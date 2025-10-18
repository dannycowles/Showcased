import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import {NgClass} from '@angular/common';

@Component({
  selector: 'yt-button-heart',
  template: `
    <div class='button_heart' [ngClass]='{on: isOn, animate: isAnimate}'
         [style]="'transform: scale(' + scale + ')'"></div>
  `,
  imports: [
    NgClass
  ],
  styles: [`
    .button_heart {
      width: 50px;
      height: 50px;
      cursor: pointer;
      background: url(https://demos.9lessons.info/twitterHeart/images/web_heart_animation.png) no-repeat left;
      background-size: 1450px;
      transform-origin: center;
    }

    .button_heart.on {
      background-position: right;
    }

    .button_heart.on.animate {
      -webkit-animation-name: heartBlast;
      animation-name: heartBlast;
      -webkit-animation-duration: .8s;
      animation-duration: .8s;
      -webkit-animation-iteration-count: 1;
      animation-iteration-count: 1;
      -webkit-animation-timing-function: steps(28);
      animation-timing-function: steps(28);
    }

    @-webkit-keyframes heartBlast {
      0% {
        background-position: left;
      }
      100% {
        background-position: right;
      }
    }

    @keyframes heartBlast {
      0% {
        background-position: left;
      }
      100% {
        background-position: right;
      }
    }
  `]
})
export class ButtonHeartComponent implements OnInit {

  @Input() isLiked: boolean = false;
  @Input() size: number = 50;
  public scale: number = 1;
  public isOn: boolean;
  public isAnimate: boolean;

  constructor() { }

  ngOnInit(): void {
    // Check if mobile dimensions
    if (window.innerWidth <= 600) {
      this.size = 75;
    }
    this.scale = Math.round(this.size / 50 * 100) / 100;
  }

  ngOnChanges(e: any){
    var isLiked = e.isLiked;
    if(!isLiked.currentValue){ this.favOff(); }
    else{
      if(!!isLiked.firstChange){ this.favOn(); }
      else{ this.favOn(true); }
    }
  }

  favOn(isAnimated: boolean = false){
    if(isAnimated){ this.isAnimate = true; }
    this.isOn = true;
  }
  favOff(){
    this.isOn = false;
    this.isAnimate = false;
  }
}
