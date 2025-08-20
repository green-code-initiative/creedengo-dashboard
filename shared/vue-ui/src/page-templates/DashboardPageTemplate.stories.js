import ScorePage from "./ScorePage.vue";

export default {
  title: "Pages/ScorePage",
  component: ScorePage,
  tags: ['autodocs']
};

export const Default = {
  render: () => ({
    components: { ScorePage },
    template: "<ScorePage />",
  }),
};
