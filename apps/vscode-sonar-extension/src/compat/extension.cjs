// CommonJS wrapper module triggers loading of the ES client module
// Thanks to Jan Miksovsky
// See https://jan.miksovsky.com/posts/2025/03-17-vs-code-extension

let extension;

// Create a proxy extension that just calls the one in the ES module
module.exports = {
  async activate(context) {
    // Load the ES module if it hasn't been loaded yet
    extension ??= await import("../extension.mjs");
    // Delegate to the ES module's activate method
    extension.activate(context);
  },
  deactivate() {
    // Delegate to the ES module's activate method
    // The ES module should have been loaded on activate
    extension?.deactivate()
  }
};