import { http, HttpResponse, delay } from 'msw';
import { sonarMswHandler } from '@creedengo/sonar-services';

import ScoreWidget from './ScoreWidget.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Widgets/ScoreWidget',
  component: ScoreWidget,
  tags: ['autodocs'],
  argTypes: {
    project: { control: { type: 'text' }, default: 'my-project' },
    branch: { control: { type: 'text' }, default: 'main' },
  },
  parameters: { msw: { handlers: sonarMswHandler } },
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