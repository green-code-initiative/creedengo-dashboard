import Dashboard from './DashboardPage.vue';

import { http, HttpResponse, delay } from 'msw';
import sonar from '@creedengo/sonar-services'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Pages/Dashboard',
  component: Dashboard,
  tags: ['autodocs'],
  argTypes: {
    projectKey: { control: { type: 'text' }, default: 'my-project' },
    branch: { control: { type: 'text' }, default: 'main' },
  },
  parameters: { msw: { handlers: sonar.handlers } },
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
