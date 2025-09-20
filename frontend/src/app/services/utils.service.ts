import {Injectable} from '@angular/core';
import {GenreData} from '../data/show/genre-data';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  getFormattedDate(date: Date): string {
    const months = ["January", "February", "March", "April", "May", "June", "July",
    "August", "September", "October", "November", "December"];

    const month = months[date.getMonth()];
    const day = date.getDate();
    const year = date.getFullYear();

    let daySuffix;
    if (day === 1 || day === 21 || day === 31) {
      daySuffix = "st";
    } else if (day === 2 || day === 22) {
      daySuffix = "nd";
    } else if (day === 3 || day === 23) {
      daySuffix = "rd";
    } else {
      daySuffix = "th";
    }

    return `${month} ${day}${daySuffix}, ${year}`;
  }

  debounce = (fn: Function, ms = 300) => {
    let timeoutId: ReturnType<typeof setTimeout>;
    return function (this: any, ...args: any[]) {
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => fn.apply(this, args), ms);
    };
  };

  displayCharactersLeft(inputID: string, helpBlockID: string, maxLength: number) {
    const inputElement = document.getElementById(inputID) as HTMLInputElement | HTMLTextAreaElement;
    const helpBlockElement = document.getElementById(helpBlockID);

    if (!inputElement || !helpBlockElement) return;

    const charactersLeft = maxLength - inputElement.value.length;
    helpBlockElement.innerText = `${charactersLeft} characters remaining`;
  }

  getRelativeDate(dateString: string): string {
    const actualDate = new Date(dateString);
    const diffInSeconds = Math.floor(new Date().getTime() - actualDate.getTime()) / 1000;
    if (diffInSeconds < 60) {
      return "Just now";
    }

    const diffInMinutes = Math.floor(diffInSeconds / 60);
    if (diffInMinutes < 60) {
      return `${diffInMinutes} minute${diffInMinutes === 1 ? "" : "s"} ago`;
    }

    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) {
      return `${diffInHours} hour${diffInHours === 1 ? "" : "s"} ago`;
    }

    const diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays < 7) {
      return `${diffInDays} day${diffInDays === 1 ? "" : "s"} ago`;
    }

    // For dates longer than one week, just display the formatted date
    return this.getFormattedDate(actualDate);
  }

  getFormattedRuntime(runtime: number): string {
    if (runtime < 60) {
      return `${runtime}m`;
    }

    const hours = Math.floor(runtime / 60);
    const minutes = runtime % 60;

    return minutes === 0 ? `${hours}h` : `${hours}h ${minutes}m`;
  }
}
