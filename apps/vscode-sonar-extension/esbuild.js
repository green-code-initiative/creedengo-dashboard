const fs = require('node:fs');

const esbuild = require('esbuild');

const production = process.argv.includes('--production');
const watch = process.argv.includes('--watch');

/**
 * @param {string} entryPoint 
 * @param {Object} [options]
 * @param {string} [options.outfile]
 * @param {'browser'|'node'|'neutral'} [options.platform]
 * @param {'iife'|'cjs'|'esm'} [options.format]
 * @param {'silent'|'error'|'warning'|'info'|'debug'|'verbose'} [options.logLevel]
 * @returns 
 */
function addContext(entryPoint, options) {
  const { outfile, platform, format, logLevel = 'warning' } = options
  const outdir = 'dist'
  const outExtension = { '.js': '.mjs' }
  const outConfig = outfile ? { outfile: `${outdir}/${outfile}`} : { outdir, outExtension }
  const importMetaPolyfill = { 'import.meta': 'importMeta' }
  const polyfill = {}
  if (format === 'cjs' && platform === 'node') {
    polyfill.inject = ['src/compat/import.meta-polyfill.cjs']
    polyfill.define = importMetaPolyfill
  }
  return {
    entryPoints: [entryPoint],
    bundle: true,
    format,
    minify: production,
    sourcemap: !production,
    sourcesContent: false,
    platform,
    ...polyfill,
    ...outConfig,
    external: ['vscode'],
    logLevel,
    plugins: [
      /* add to the end of plugins array */
      esbuildProblemMatcherPlugin
    ]
  }
} 
async function main() {
  const { default: cpy } = await import('cpy')

  const ctxList = await Promise.all([
    esbuild.context(
      addContext('src/compat/extension.cjs', { outfile: 'extension.js', format: 'cjs', platform: 'node'})
    ),
    esbuild.context(
      addContext('src/**/*.script.mjs', { platform: 'browser', format: 'esm' })
    )
  ]);
  await cpy('src/**/*.(html|png)', 'dist', { flat: true})
  if (production) {
    await cpy('../../LICENSE.md', './LICENSE')
  }

  if (watch) {
    await Promise.all(ctxList.map(ctx => ctx.watch()));
  } else {
    await Promise.all(ctxList.map(ctx => ctx.rebuild()));
    await Promise.all(ctxList.map(ctx => ctx.dispose()));
  }
}

/**
 * @type {import('esbuild').Plugin}
 */
const esbuildProblemMatcherPlugin = {
  name: 'esbuild-problem-matcher',

  setup(build) {
    build.onStart(() => {
      console.log('[watch] build started');
    });
    build.onEnd(result => {
      result.errors.forEach(({ text, location }) => {
        console.error(`✘ [ERROR] ${text}`);
        if (location == null) return;
        console.error(`    ${location.file}:${location.line}:${location.column}:`);
      });
      console.log('[watch] build finished');
    });
  }
};

main().catch(e => {
  console.error(e);
  process.exit(1);
});