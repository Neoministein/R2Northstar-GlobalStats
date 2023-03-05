import {AppConfig} from "../AppConfig";

export interface PlayerBucket {
    key: string
    playerName: string
}

export interface PlayerKillBucket extends PlayerBucket {
    PGS_PILOT_KILLS: number
}

export interface PlayerKdBucket extends PlayerBucket {
    kd: number
    PGS_PILOT_KILLS: number
    PGS_DEATHS: number
}

export interface NpcKillBucket extends PlayerBucket {
    PGS_NPC_KILLS: number
}

export interface WinRatioBucket extends PlayerBucket {
    ratio: number
    filters: WinRatioFilter
}

export interface WinRatioFilter {
    lose: number
    win: number
}

export interface WinsBucket extends PlayerBucket {
    win: number
}

export interface PlayerLookUp {
    uId: string
    playerName: string
}

type CachedPlayerLookup = PlayerLookUp | Promise<PlayerLookUp>;

const BackendService = {

    playerCache: new Map<string, CachedPlayerLookup>(),

    getTopPlayerKills(tags : string[] = []) : Promise<PlayerKillBucket[]> {
        return fetch(AppConfig.apiUrl + "/result/top/player-kills?max=1000" + this.formatTagParam(tags)).then(response => response.json())
        .then(data => data.buckets);
    },

    getTopPlayerKd(tags : string[] = []) : Promise<PlayerKdBucket[]> {
        return fetch(AppConfig.apiUrl + "/result/top/player-kd?max=1000" + this.formatTagParam(tags)).then(response => response.json())
        .then(data => data.buckets);
    },

    getTopNpcKills(tags : string[] = []) : Promise<NpcKillBucket[]> {
        return fetch(AppConfig.apiUrl + "/result/top/npc-kills?max=1000" + this.formatTagParam(tags)).then(response => response.json())
        .then(data => data.buckets);
    },

    getTopWinRatio(tags : string[] = []) : Promise<WinRatioBucket[]> {
        return fetch(AppConfig.apiUrl + "/result/top/win-ratio?max=1000" + this.formatTagParam(tags)).then(response => response.json())
        .then(data => data.buckets);
    },

    getTopWins(tags : string[] = []) : Promise<WinsBucket[]> {
        return fetch(AppConfig.apiUrl + "/result/top/win?max=1000" + this.formatTagParam(tags)).then(response => response.json())
        .then(data => data.buckets);
    },

    getPlayerNameFromUid(uid : string) : Promise<PlayerLookUp> {
        const cachedPlayer = this.playerCache.get(uid);
        if(cachedPlayer === undefined) {
            const promise = fetch(AppConfig.apiUrl + "/player/uid/" + uid ).then(response => response.json()).then(playerLookUp => {
                this.playerCache.set(uid, playerLookUp);
                return playerLookUp;
            });

            this.playerCache.set(uid, promise);

            return promise;
        } else if (cachedPlayer instanceof Promise) {
            return cachedPlayer;
        }

        return Promise.resolve(cachedPlayer);
    },

    formatTagParam(tags : string[]) : string {
        if(tags.length === 0) {
            return "";
        }

        return "&tags=" + tags.join(",");
    }
}

export default BackendService;
