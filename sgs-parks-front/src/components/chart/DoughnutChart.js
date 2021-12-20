import {Doughnut} from "vue-chartjs";

export default {
    extends: Doughnut,
    props: ["data","options"],
    data: () => ({
        html: null
    }),
    mounted() {
        this.renderChart(this.data, this.options);
        this.html = this.$data._chart.generateLegend();
    },
    methods: {
        reRender() {
            this.renderChart(this.data, this.options);
            this.html = this.$data._chart.generateLegend();
        }
    }
}