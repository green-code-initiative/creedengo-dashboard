// THIS LOGIC MIGHT MOVE LATER TO PARENT APPS
export const isSonarPlugin = Boolean(globalThis.SonarRequest)
export const isWebExtension = (globalThis.chrome || globalThis?.browser?.action?.setTitle)
export const isWebsite = !isSonarPlugin && !isWebExtension
