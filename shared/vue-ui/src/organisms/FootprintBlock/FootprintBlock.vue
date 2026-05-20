<script setup>
import TooltipBox from '../../molecules/TooltipBox/TooltipBox.vue';
defineProps({
  score: { type: Number, required: true },
});
const formula = "nGd / mLoc * f * nServer * ts * ((eManufacturing / lifetime) + (ed * ci))"
const listLabels = ["nGd: Number of 'Green Deficiencies' detected", 
      "mLoc: Millions of Lines of Code",
      "f: Application type factor to calculate potential reductions if green deficiencies are fixed as observed in the study conducted by CAST Research Labs",
      "nServer: Number of servers that the application utilizes to operate",
      "ts: 	The 'Time Sharing' or percent utilization of the server resource capacity",
      "eManufacturing: The CO2 'Emissions' during manufacturing of servers (in kgCO2e)",
      "lifetime: The expected 'Lifetime' of the servers (in years)",
      "ed: The annual 'Energy Demand' for each server (in kWh/year)",
      "ci: The 'Carbon Intensity' of the environment where the application is running"]
</script>

<template>
  <h2>Your footprint score is : {{ score }} <span class="score">kgCO2 eq potential emission reductions per year</span></h2>
  <div class="tooltip-calc">
    how my score is calculated ?
    <tooltip-box>
      <p>
        <strong>{{ formula }} </strong><br>
        <ul>
          <li
            v-for="(value, index) in listLabels"
            :key="index"
          >
            {{ value }}
          </li>
        </ul>
      </p>
    </tooltip-box>
  </div>
</template>

<style scoped>
h2 {
  margin-top: 30px;
}
.score {
  font-size: 16px;
}
.tooltip-calc {
  margin-top: 20px;
  margin-bottom: 1.25rem;
  text-decoration: underline;
  display: inline-block;
}
</style>
