package com.example.administrator.musicplayer.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */

public class SearchSongList {

    /**
     * song : [{"bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","weight":"130","songname":"当年情","songid":"116780440","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"王若琳","info":"Love from Once Upon a Time","resource_provider":"1","control":"0000000000","encrypted_songid":"40076f5ed980958482576L"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"0","songname":"当年情 cover.Leslie","songid":"74199956","has_mv":"0","yyr_artist":"1","resource_type_ext":"0","artistname":"BrokenDreams细杰","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":""},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"0","songname":"《当年情》","songid":"74055781","has_mv":"0","yyr_artist":"1","resource_type_ext":"0","artistname":"陈东东","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":""}]
     * error_code : 22000
     * order : song
     */

    private int error_code;
    private String order;
    private List<SongBean> song;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<SongBean> getSong() {
        return song;
    }

    public void setSong(List<SongBean> song) {
        this.song = song;
    }

    public static class SongBean {
        /**
         * bitrate_fee : {"0":"129|-1","1":"-1|-1"}
         * weight : 130
         * songname : 当年情
         * songid : 116780440
         * has_mv : 0
         * yyr_artist : 0
         * resource_type_ext : 0
         * artistname : 王若琳
         * info : Love from Once Upon a Time
         * resource_provider : 1
         * control : 0000000000
         * encrypted_songid : 40076f5ed980958482576L
         */

        private String bitrate_fee;
        private String weight;
        private String songname;
        private String songid;
        private String has_mv;
        private String yyr_artist;
        private String resource_type_ext;
        private String artistname;
        private String info;
        private String resource_provider;
        private String control;
        private String encrypted_songid;

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public void setBitrate_fee(String bitrate_fee) {
            this.bitrate_fee = bitrate_fee;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }

        public String getHas_mv() {
            return has_mv;
        }

        public void setHas_mv(String has_mv) {
            this.has_mv = has_mv;
        }

        public String getYyr_artist() {
            return yyr_artist;
        }

        public void setYyr_artist(String yyr_artist) {
            this.yyr_artist = yyr_artist;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getResource_provider() {
            return resource_provider;
        }

        public void setResource_provider(String resource_provider) {
            this.resource_provider = resource_provider;
        }

        public String getControl() {
            return control;
        }

        public void setControl(String control) {
            this.control = control;
        }

        public String getEncrypted_songid() {
            return encrypted_songid;
        }

        public void setEncrypted_songid(String encrypted_songid) {
            this.encrypted_songid = encrypted_songid;
        }
    }
}
