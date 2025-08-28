import {Component, Input, OnChanges, ViewChild} from '@angular/core';
import {BaseChartDirective} from 'ng2-charts';
import {ChartConfiguration} from 'chart.js';
import {ReviewBreakdown} from '../../data/show/show-data';

@Component({
  selector: 'app-review-chart',
  imports: [BaseChartDirective],
  templateUrl: './review-chart.component.html',
  styleUrl: './review-chart.component.css',
  standalone: true
})
export class ReviewChartComponent implements OnChanges {
  readonly barChartOptions: ChartConfiguration<'bar'>['options'] = {
    datasets: {
      bar: {
        categoryPercentage: 1,
        barPercentage: 1,
        borderRadius: 4
      }
    },
    scales: {
      x: {
        grid: {
          display: false
        }
      },
      y: {
        display: false,
        grid: {
          display: false
        }
      }
    },
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      tooltip: {
        displayColors: false,
        callbacks: {
          title: () => '',
          label: (context) => {
            const rating = context.label;
            const count = context.parsed.y;
            return `${rating}⭐ : ${count} reviews`;
          }
        }
      }
    }
  };

  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10' ],
    datasets: [
      {
        data: [],
        backgroundColor: 'grey',
        borderWidth: 1
      }
    ]
  };

  @Input({required: true}) reviewDistribution: ReviewBreakdown[];
  reviewCount: number;
  reviewAverage: number;
  @ViewChild(BaseChartDirective) chart: BaseChartDirective;

  ngOnChanges() {
    this.barChartData.datasets[0].data = this.reviewDistribution.map(review => review.numReviews);
    this.computeReviewAverage();
    this.chart?.update();
  }

  computeReviewAverage() {
    const weightedSum = this.reviewDistribution.reduce((sum, review) => sum + (review.rating * review.numReviews), 0);
    this.reviewCount = this.reviewDistribution.reduce((sum, review) => sum + review.numReviews, 0);
    this.reviewAverage =  parseFloat((weightedSum / this.reviewCount).toFixed(1));
  }
}
