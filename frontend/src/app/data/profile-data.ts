import {ReviewData} from './review-data'
import {UtilsService} from '../services/utils.service'
import {ShowRankingData} from './lists/show-ranking-data'
import {EpisodeRankingData} from './lists/episode-ranking-data'
import {SeasonRankingData} from './lists/season-ranking-data'
import {CharacterRankingData} from './lists/character-ranking-data';
import {UserSocialData} from './user-social-data';
import {UserHeaderData} from './user-header-data';
import {ShowListData} from './lists/show-list-data';

export class ProfileData {
  readonly username: string
  profilePicture: string
  bio: string
  readonly watchlistTop: ShowListData[]
  readonly moreWatchlist: boolean
  readonly watchingTop: ShowListData[]
  readonly moreWatching: boolean
  readonly showRankingTop: ShowRankingData[]
  readonly moreShowRanking: boolean
  readonly episodeRankingTop: EpisodeRankingData[]
  readonly moreEpisodeRanking: boolean
  readonly reviews: ReviewData[]
  numFollowers: number
  numFollowing: number
  isFollowing: boolean
  isOwnProfile: boolean
  readonly seasonRankingTop: SeasonRankingData[]
  readonly protagonistRankingTop: CharacterRankingData[]
  readonly deuteragonistRankingTop: CharacterRankingData[]
  readonly antagonistRankingTop: CharacterRankingData[]
  readonly tritagonistRankingTop: CharacterRankingData[]
  readonly sideCharacterRankingTop: CharacterRankingData[]
  socials: UserSocialData[];

  constructor(jsonObject: { [key : string]: any }) {
    this.username = jsonObject['username']
    this.profilePicture = jsonObject['profilePicture']
    this.bio = jsonObject['bio']

    this.watchlistTop = jsonObject['watchlistTop'].map(( show: {} ) => {
      return new ShowListData(show)
    })
    this.moreWatchlist = jsonObject['moreWatchlist']

    this.watchingTop = jsonObject['watchingTop'].map(( show: {} ) => {
      return new ShowListData(show)
    })
    this.moreWatching = jsonObject['moreWatching']

    this.showRankingTop = jsonObject['showRankingTop'].map(( show: {} ) => {
      return new ShowRankingData(show)
    })
    this.moreShowRanking = jsonObject['moreShowRanking']

    this.episodeRankingTop = jsonObject['episodeRankingTop'].map(( episode: {} ) => {
      return new EpisodeRankingData(episode)
    })
    this.moreEpisodeRanking = jsonObject['moreEpisodeRanking']

    this.reviews = jsonObject['reviews'].map(( review: {} ) => {
      return new ReviewData(review, new UtilsService())
    })

    this.numFollowers = jsonObject['numFollowers']
    this.numFollowing = jsonObject['numFollowing']
    this.isFollowing = jsonObject['following']
    this.isOwnProfile = jsonObject['ownProfile']

    this.seasonRankingTop = jsonObject['seasonRankingTop'].map(( season: {} ) => {
      return new SeasonRankingData(season)
    })

    this.protagonistRankingTop = jsonObject['characterRankings']['protagonists'].map(( protagonist: {} ) => {
      return new CharacterRankingData(protagonist)
    })
    this.deuteragonistRankingTop = jsonObject['characterRankings']['deuteragonists'].map(( deuteragonist: {} ) => {
      return new CharacterRankingData(deuteragonist)
    })
    this.antagonistRankingTop = jsonObject['characterRankings']['antagonists'].map(( antagonist: {} ) => {
      return new CharacterRankingData(antagonist)
    })

    this.socials = jsonObject['socialAccounts'].map(( socialAccount: {} ) => {
      return new UserSocialData(socialAccount)
    })

    this.tritagonistRankingTop = jsonObject['characterRankings']['tritagonists'].map(( tritagonist: {} ) => {
      return new CharacterRankingData(tritagonist)
    })

    this.sideCharacterRankingTop = jsonObject['characterRankings']['side'].map(( sideCharacter: {} ) => {
      return new CharacterRankingData(sideCharacter)
    })
  }

  get userHeaderData(): UserHeaderData {
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
  }
}
