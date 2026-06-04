import { vi, afterAll, beforeAll, expect, test } from 'vitest'
import api from './adapter.js' 
import { getFootprintEstimation } from './footprint.service'
import { describe } from 'vitest';


vi.mock('./adapter', async () => ({ default: {
    services: {
        getIssueCount: vi.fn(),
        getNumberOfLineOfCode: vi.fn()
    }
}}))

beforeAll(() => {
  // Mock the API calls
  api.services.getIssueCount.mockResolvedValue(5);
  api.services.getNumberOfLineOfCode.mockResolvedValue(100);
});

afterAll(() => {
  // Clean the API calls
});

describe('getFootprintEstimation()', () =>
    test('should calculate footprint estimation with given configuration', async () => {
        const config = {
            project: "dashboard",
            branch: "142",
            nServer: 1,
            timeSharing: 0.5,
            eManufacturing: 100,
            lifetime: 5,
            energyDemand: 1670,
            carbonIntensity: 0.5         
        };

        const estimation = await getFootprintEstimation(config)        
        
        expect(estimation).toBeCloseTo(0.000855, 6)
    }),
    test('should calculate footprint estimation with default values', async () => {
        const config = {
            project: "dashboard",
            branch: "142",   
        };

        const estimation = await getFootprintEstimation(config)        
        
        expect(estimation).toBeCloseTo(0.00384672, 8)
    })
)