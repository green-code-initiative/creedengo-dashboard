#!/usr/bin/env python3
"""
Capture SonarQube API responses as fixture files for MockSonarServer.

Usage:
    python3 capture-sonar-fixtures.py [--url URL] [--token TOKEN] [--project PROJECT]

Environment variables (fallback if CLI args not given):
    SONAR_URL    Base URL of SonarQube instance  (default: http://localhost:9000)
    SONAR_TOKEN  Authentication bearer token
    SONAR_PROJECT Project key                    (default: org.green-code-initiative:creedengo-java-plugin-test-project)

Captured fixtures are written to:
    src/test/resources/fixtures/sonar/
"""

import argparse
import json
import os
import sys
import urllib.request
import urllib.parse
from pathlib import Path

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------

DEFAULT_URL     = "http://localhost:9000"
DEFAULT_PROJECT = "org.green-code-initiative:creedengo-java-plugin-test-project"
DEFAULT_BRANCH  = "main"
OUT_DIR         = Path(__file__).parent / "src/test/resources/fixtures/sonar"

# Fields to keep for each issue (verbose ones like flows/msgFormattings are dropped)
ISSUE_FIELDS = [
    "key", "rule", "severity", "component", "project", "line",
    "status", "message", "effort", "debt", "tags",
    "creationDate", "updateDate", "type", "scope",
    "cleanCodeAttribute", "cleanCodeAttributeCategory",
    "impacts", "issueStatus",
]

# Fields to keep for each rule (descriptionSections can be huge)
RULE_FIELDS = [
    "key", "repo", "name", "severity", "status",
    "tags", "sysTags", "lang", "langName", "type",
    "defaultRemFnBaseEffort", "remFnBaseEffort", "scope",
    "cleanCodeAttribute", "cleanCodeAttributeCategory", "impacts",
]

# ---------------------------------------------------------------------------
# HTTP helpers
# ---------------------------------------------------------------------------

def fetch(base_url: str, token: str, route: str, params: dict) -> dict:
    query = urllib.parse.urlencode(params)
    url = f"{base_url.rstrip('/')}/{route}?{query}"
    req = urllib.request.Request(url, headers={"Authorization": f"Bearer {token}"})
    try:
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read().decode())
    except urllib.error.HTTPError as exc:
        body = exc.read().decode()
        print(f"  HTTP {exc.code} for {route}: {body}", file=sys.stderr)
        raise


def save(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"  Saved {path.relative_to(Path.cwd()) if path.is_relative_to(Path.cwd()) else path}")


# ---------------------------------------------------------------------------
# Capture functions
# ---------------------------------------------------------------------------

def capture_issues(base_url, token, project, branch, out_dir: Path) -> None:
    print("→ api/issues/search (tags=sustainability, facets=severities,types) …")
    data = fetch(base_url, token, "api/issues/search", {
        "componentKeys": project,
        "branch":        branch,
        "resolved":      "false",
        "tags":          "sustainability",
        "facets":        "severities,types",
        "ps":            "500",
    })

    # Keep 3 issues with distinct rules
    seen_rules: set = set()
    kept = []
    for issue in data.get("issues", []):
        rule = issue.get("rule", "")
        if rule not in seen_rules:
            seen_rules.add(rule)
            kept.append({k: issue[k] for k in ISSUE_FIELDS if k in issue})
        if len(kept) == 3:
            break

    # Keep only TRK/FIL components
    components = [
        c for c in data.get("components", [])
        if c.get("qualifier") in ("TRK", "FIL")
    ][:2]

    out = {
        "total":      data["total"],
        "p":          1,
        "ps":         100,
        "paging":     {"pageIndex": 1, "pageSize": 100, "total": data["total"]},
        "effortTotal": data.get("effortTotal"),
        "issues":     kept,
        "components": components,
        "facets":     data.get("facets", []),
    }
    save(out_dir / "issues-search.json", out)


def capture_measures(base_url, token, project, branch, out_dir: Path) -> None:
    print("→ api/measures/component …")
    metrics = ",".join([
        "ncloc", "coverage", "bugs", "vulnerabilities", "code_smells",
        "sqale_index", "alert_status", "reliability_rating",
        "security_rating", "sqale_rating",
    ])
    data = fetch(base_url, token, "api/measures/component", {
        "component":  project,
        "branch":     branch,
        "metricKeys": metrics,
    })
    save(out_dir / "measures-component.json", data)


def capture_branches(base_url, token, project, out_dir: Path) -> None:
    print("→ api/project_branches/list …")
    data = fetch(base_url, token, "api/project_branches/list", {"project": project})

    # Augment with a synthetic non-main branch so that tests can exercise
    # multi-branch parsing even when the captured project has only one branch.
    has_non_main = any(not b.get("isMain", True) for b in data.get("branches", []))
    if not has_non_main:
        data["branches"].append({
            "name": "develop",
            "isMain": False,
            "type": "BRANCH",
            "status": {"qualityGateStatus": "OK"},
            "analysisDate": "2025-12-01T10:00:00+0100",
            "excludedFromPurge": False,
            "branchId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        })
        print("  (added synthetic 'develop' branch for multi-branch test coverage)")

    save(out_dir / "project-branches.json", data)


def capture_rules(base_url, token, project, branch, out_dir: Path) -> None:
    print("→ api/rules/search (tags=sustainability) …")
    data = fetch(base_url, token, "api/rules/search", {
        "tags":          "sustainability",
        "componentKeys": project,
        "branch":        branch,
        "ps":            "10",
    })

    kept = [{k: r[k] for k in RULE_FIELDS if k in r} for r in data.get("rules", [])[:5]]
    out = {
        "total": data["total"],
        "p":     1,
        "ps":    10,
        "rules": kept,
    }
    save(out_dir / "rules-search.json", out)

    # Capture details for the first rule (rules/show)
    if kept:
        first_key = kept[0]["key"]
        print(f"→ api/rules/show (rule={first_key}) …")
        detail_data = fetch(base_url, token, "api/rules/show", {"key": first_key})
        rule = detail_data.get("rule", {})

        # Keep only a subset of fields for the show fixture
        show_fields = RULE_FIELDS + ["htmlDesc", "mdDesc", "descriptionSections"]
        show_rule = {k: rule[k] for k in show_fields if k in rule}

        # Build a minimal htmlDesc from descriptionSections if htmlDesc is absent
        if "htmlDesc" not in show_rule:
            sections = rule.get("descriptionSections", [])
            root_cause = next((s["content"] for s in sections if s.get("key") == "root_cause"), None)
            show_rule["htmlDesc"] = root_cause or ""

        save(out_dir / "rules-show.json", {"rule": show_rule})


def capture_pull_requests(base_url, token, project, out_dir: Path) -> None:
    print("→ api/project_pull_requests/list …")
    try:
        data = fetch(base_url, token, "api/project_pull_requests/list", {"project": project})
        if "errors" in data:
            print(f"  Endpoint not available ({data['errors'][0]['msg']}); keeping existing fixture.")
            return
        save(out_dir / "project-pull-requests.json", data)
    except Exception as exc:
        print(f"  Skipped pull-requests (not available): {exc}")


# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------

def parse_args():
    parser = argparse.ArgumentParser(description=__doc__,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--url",     default=os.environ.get("SONAR_URL",     DEFAULT_URL))
    parser.add_argument("--token",   default=os.environ.get("SONAR_TOKEN",   ""))
    parser.add_argument("--project", default=os.environ.get("SONAR_PROJECT", DEFAULT_PROJECT))
    parser.add_argument("--branch",  default=DEFAULT_BRANCH)
    return parser.parse_args()


def main():
    args = parse_args()

    if not args.token:
        print("Error: Sonar token is required (--token or SONAR_TOKEN env var).", file=sys.stderr)
        sys.exit(1)

    print(f"Capturing Sonar API responses from {args.url}")
    print(f"  Project : {args.project}")
    print(f"  Branch  : {args.branch}")
    print(f"  Output  : {OUT_DIR}\n")

    capture_issues(args.url, args.token, args.project, args.branch, OUT_DIR)
    capture_measures(args.url, args.token, args.project, args.branch, OUT_DIR)
    capture_branches(args.url, args.token, args.project, OUT_DIR)
    capture_rules(args.url, args.token, args.project, args.branch, OUT_DIR)
    capture_pull_requests(args.url, args.token, args.project, OUT_DIR)

    print("\nDone. Commit the updated fixture files to version the snapshots.")


if __name__ == "__main__":
    main()
