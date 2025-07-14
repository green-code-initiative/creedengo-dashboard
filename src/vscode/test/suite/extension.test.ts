import { afterAll, describe, expect, test } from 'vitest'

// You can import and use all API from the 'vscode' module
// as well as import your extension to test it
//import { window } from 'vscode';
//import * as myExtension from '../../extension.js';


describe('Extension Test Suite', () => {
  afterAll(() => {
    //window.showInformationMessage('All tests done!');
  });

  test('Sample test', () => {
    expect(-1).toStrictEqual([1, 2, 3].indexOf(5));
    expect(-1).toStrictEqual([1, 2, 3].indexOf(0));
  });
}); 
