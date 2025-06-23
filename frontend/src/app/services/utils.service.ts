import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  getFormattedDate(date: Date): string {

    const months = ["January", "February", "March", "April", "May", "June", "July",
    "August", "September", "October", "November", "December"];

    let month = months[date.getMonth()];
    let day = date.getDate();
    let year = date.getFullYear();

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
}


/*get relativeDate():string {
  let diffInSeconds = Math.floor(new Date().getTime() - this.reviewDate.getTime()) / 1000;
  if (diffInSeconds < 60) {
    return `${diffInSeconds} seconds ago`;
  }

  let diffInMinutes = Math.floor(diffInSeconds / 60);
  if (diffInMinutes < 60) {
    if (diffInMinutes === 1) {
      return `${diffInMinutes} minute ago`;
    } else {
      return `${diffInMinutes} minutes ago`;
    }
  }

  let diffInHours = Math.floor(diffInMinutes / 60);
  if (diffInHours < 24) {
    if (diffInHours === 1) {
      return `${diffInHours} hour ago`;
    } else {
      return `${diffInHours} hours ago`;
    }
  }

  let diffInDays = Math.floor(diffInHours / 24);
  if (diffInDays < 7) {
    if (diffInDays === 1) {
      return `${diffInDays} day ago`;
    } else {
      return `${diffInDays} days ago`;
    }
  }

  return this.utilsService.getFormattedDate(this.reviewDate);
}
}*/

/*  get userHeaderData(): UserHeaderData {
    return {
      username: this.username,
      profilePicture: this.profilePicture,
      bio: this.bio,
      numFollowers: this.numFollowers,
      numFollowing: this.numFollowing,
      socials: this.socials,
      isFollowing: this.isFollowing,
      isOwnProfile: this.isOwnProfile
    };
  }*/

