import api from './adapter.js' 

async function getCastEstimation(config) {
    const { project, branch } = config
    const nGd = api.getIssueCount()
    const mLoc = api.getNumberOfLineOfCode({ project, branch })
    const f = 0.004 / 100
    const { nServer = 3 } = config
    const { timeSharing = 1 } = config
    const ts = timeSharing
    const { eManufacturing = 320 } = config
    const { lifetime = 4 } = config
    const { energyDemand = 1670 } = config
    const ed = energyDemand
    const { carbonIntensity = 0.336 } = config
    const ci = carbonIntensity
    return (await nGd / await mLoc) * f * nServer * ts * ((eManufacturing / lifetime) + (ed * ci))
}

const FORMULA_COLLECTION = {
    'cast-highlight': getCastEstimation
};

/**
 * 
 * @param {Object} config 
 * @param {string} config.project Sonar project key
 * @param {string} [config.branch] Sonar branch name
 * @param {string} [config.formula] Formula type used to calculate the footprint (default: 'cast-highlight')
 * @param {number} [config.nServer] Number of servers that the application utilizes to operate (default: 3)
 * @param {number} [config.timeSharing] The Time Sharing or percent utilization of the server resource capacity (default: 1 for 100%)
 * @param {number} [config.eManufacturing] The CO2eq Emissions during manufacturing of servers (default = 320 kgCO2eq for a DELL PowerEdge R640)
 * @param {number} [config.lifetime] The expected "lifetime" of the servers in years (default: 4)
 * @param {number} [config.energyDemand] The annual "Energy Demand" for each server (default: 1670 kWh/year for a DELL PowerEdge R640)
 * @param {number} [config.carbonIntensity] The "Carbon Intensity" of the environment where the application is running (default: 0.336 kgCO2/kWh for North America)
 * @returns 
 */
export function getFootprintEstimation(config) {
    const { formula = 'cast-highlight' } = config
    return FORMULA_COLLECTION[formula](config)
}