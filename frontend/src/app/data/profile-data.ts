import {ReviewData} from './review-data'
import {UtilsService} from '../services/utils.service'
import {WatchlistData} from './lists/watchlist-data'
import {WatchingData} from './lists/watching-data'
import {ShowRankingData} from './lists/show-ranking-data'
import {EpisodeRankingData} from './lists/episode-ranking-data'
import {SeasonRankingData} from './lists/season-ranking-data'
import {CharacterRankingData} from './lists/character-ranking-data';

export class ProfileData {
  readonly username: string
  profilePicture: string
  bio: string
  readonly watchlistTop: WatchlistData[]
  readonly moreWatchlist: boolean
  readonly watchingTop: WatchingData[]
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

  constructor(jsonObject: { [key : string]: any }) {
    this.username = jsonObject['username']
    this.profilePicture = jsonObject['profilePicture']
    this.bio = jsonObject['bio']

    this.watchlistTop = jsonObject['watchlistTop'].map(( show: {} ) => {
      return new WatchlistData(show)
    })
    this.moreWatchlist = jsonObject['moreWatchlist']

    this.watchingTop = jsonObject['watchingTop'].map(( show: {} ) => {
      return new WatchingData(show)
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
  }
}
