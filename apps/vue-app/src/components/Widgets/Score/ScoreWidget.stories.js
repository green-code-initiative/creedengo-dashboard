import { http, HttpResponse, delay } from 'msw';
import mockIssueList from '@/api/sonar/issues/sonar.issues.search.mock';
import mockComponentMeasuresList from '@/api/sonar/measures/sonar.measures.component.mock';

import ScoreWidget from './ScoreWidget.vue'

const handlers = [
  http.get('/api/issues/search', () => HttpResponse.json(mockIssueList)),
  http.get('/api/measures/component', () => HttpResponse.json(mockComponentMeasuresList)),
];

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Widgets/ScoreWidget',
  component: ScoreWidget,
  tags: ['autodocs'],
  argTypes: {
    projectKey: { control: { type: 'text' }, default: 'my-project' },
    branch: { control: { type: 'text' }, default: 'main' },
  },
  parameters: { msw: { handlers } },
}

export const MockedSuccess = {};

async function forbiddenErrorHandler() {
  await delay(800);
  return new HttpResponse(null, { status: 403 });
}

export const MockedError = {
  parameters: {
    msw: {
      handlers: [
        http.get('/api/issues/search', forbiddenErrorHandler),
        http.get('/api/measures/component', forbiddenErrorHandler),
      ],
    },
  },
};