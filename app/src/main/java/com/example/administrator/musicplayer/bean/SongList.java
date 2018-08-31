package com.example.administrator.musicplayer.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SongList {
    /**
     * song_list : [{"author":"G.E.M.邓紫棋","all_artist_ting_uid":"7898","title":"泡沫","song_id":"14945107","del_status":"0","album_id":"13897642","pic_small":"http://musicdata.baidu.com/data2/pic/89123644/89123644.jpg@s_0,w_90","resource_type":"0","all_artist_id":"1814","artist_id":"1814","high_rate":"320","all_rate":"64,128,192,256,320,flac","has_mv":1,"has_mv_mobile":1,"havehigh":2,"copy_type":"1","charge":0,"learn":1,"versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","info":"泡沫-G.E.M.邓紫棋","has_filmtv":"0","ting_uid":"7898","album_title":"Xposed(曝光)","is_first_publish":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"1100000000","artist_name":"G.E.M.邓紫棋","rank":"2","biaoshi":""},{"author":"平安","all_artist_ting_uid":"12892072","title":"在舞台中间","song_id":"122121834","del_status":"0","album_id":"122157804","pic_small":"http://musicdata.baidu.com/data2/pic/122157784/122157784.jpg@s_0,w_90","resource_type":"0","all_artist_id":"16566595","artist_id":"16566595","high_rate":"320","all_rate":"24,64,128,192,256,320,flac","has_mv":0,"has_mv_mobile":0,"havehigh":2,"copy_type":"1","charge":0,"learn":0,"versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","info":"","has_filmtv":"0","ting_uid":"12892072","album_title":"平安 ","is_first_publish":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"平安","rank":"4","biaoshi":""},{"author":"曹轩宾","all_artist_ting_uid":"10595","title":"耀出色","song_id":"104004097","del_status":"0","album_id":"99895373","pic_small":"http://musicdata.baidu.com/data2/pic/104003759/104003759.jpg@s_0,w_90","resource_type":"0","all_artist_id":"964","artist_id":"964","high_rate":"256","all_rate":"256,128,64","has_mv":1,"has_mv_mobile":1,"havehigh":0,"copy_type":"1","charge":0,"learn":0,"versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","info":"","has_filmtv":"0","ting_uid":"10595","album_title":"参宿七","is_first_publish":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0100000000","artist_name":"曹轩宾","rank":"6","biaoshi":""},{"author":"金贵晟","all_artist_ting_uid":"60866516","title":"I Believe 爱不离","song_id":"124073782","del_status":"0","album_id":"124073783","pic_small":"http://musicdata.baidu.com/data2/music/CC0EDE4DE1E7AA04C8E050861BE70135/254924039/254924039.jpg@s_0,w_90","resource_type":"0","all_artist_id":"33894855","artist_id":"33894855","high_rate":"320","all_rate":"24,64,128,192,256,320","has_mv":0,"has_mv_mobile":0,"havehigh":2,"copy_type":"1","charge":0,"learn":0,"versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","info":"","has_filmtv":"0","ting_uid":"60866516","album_title":"I Believe 爱不离","is_first_publish":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"金贵晟","rank":"9","biaoshi":""},{"author":"周杰伦","all_artist_ting_uid":"7994","title":"东风破","song_id":"10526013","del_status":"0","album_id":"10525825","pic_small":"http://musicdata.baidu.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_48","resource_type":"0","all_artist_id":"29","artist_id":"29","high_rate":"320","all_rate":"64,128,192,256,320,flac","has_mv":1,"has_mv_mobile":0,"havehigh":2,"copy_type":"1","charge":0,"learn":0,"versions":"","bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","info":"","has_filmtv":"0","ting_uid":"7994","album_title":"星座圣典","is_first_publish":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"1000000000","artist_name":"周杰伦","rank":"12","biaoshi":"vip"}]
     * billboard : {"billboard_type":9,"billboard_no":"","update_date":"2017-06-20","havemore":1,"name":"雪碧音碰音榜","comment":"","pic_s640":"http://b.hiphotos.baidu.com/ting/pic/item/ae51f3deb48f8c545a9d33da38292df5e1fe7ffd.jpg","pic_s444":"http://b.hiphotos.baidu.com/ting/pic/item/18d8bc3eb13533fa532f7aa9aad3fd1f40345b98.jpg","pic_s260":"http://a.hiphotos.baidu.com/ting/pic/item/8b82b9014a90f603542975c03b12b31bb151ed98.jpg","pic_s210":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_2455f6326a3a235f45a7735c628ed0ac.jpg","web_url":"http://music.baidu.com/topic/cooperation/sprite"}
     * error_code : 22000
     */

    private BillboardBean billboard;
    private int error_code;
    private List<SongListBean> song_list;

    public BillboardBean getBillboard() {
        return billboard;
    }

    public void setBillboard(BillboardBean billboard) {
        this.billboard = billboard;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<SongListBean> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongListBean> song_list) {
        this.song_list = song_list;
    }

    public static class BillboardBean {
        /**
         * billboard_type : 9
         * billboard_no :
         * update_date : 2017-06-20
         * havemore : 1
         * name : 雪碧音碰音榜
         * comment :
         * pic_s640 : http://b.hiphotos.baidu.com/ting/pic/item/ae51f3deb48f8c545a9d33da38292df5e1fe7ffd.jpg
         * pic_s444 : http://b.hiphotos.baidu.com/ting/pic/item/18d8bc3eb13533fa532f7aa9aad3fd1f40345b98.jpg
         * pic_s260 : http://a.hiphotos.baidu.com/ting/pic/item/8b82b9014a90f603542975c03b12b31bb151ed98.jpg
         * pic_s210 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_2455f6326a3a235f45a7735c628ed0ac.jpg
         * web_url : http://music.baidu.com/topic/cooperation/sprite
         */

        private int billboard_type;
        private String billboard_no;
        private String update_date;
        private int havemore;
        private String name;
        private String comment;
        private String pic_s640;
        private String pic_s444;
        private String pic_s260;
        private String pic_s210;
        private String web_url;

        public int getBillboard_type() {
            return billboard_type;
        }

        public void setBillboard_type(int billboard_type) {
            this.billboard_type = billboard_type;
        }

        public String getBillboard_no() {
            return billboard_no;
        }

        public void setBillboard_no(String billboard_no) {
            this.billboard_no = billboard_no;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
        }

        public int getHavemore() {
            return havemore;
        }

        public void setHavemore(int havemore) {
            this.havemore = havemore;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPic_s640() {
            return pic_s640;
        }

        public void setPic_s640(String pic_s640) {
            this.pic_s640 = pic_s640;
        }

        public String getPic_s444() {
            return pic_s444;
        }

        public void setPic_s444(String pic_s444) {
            this.pic_s444 = pic_s444;
        }

        public String getPic_s260() {
            return pic_s260;
        }

        public void setPic_s260(String pic_s260) {
            this.pic_s260 = pic_s260;
        }

        public String getPic_s210() {
            return pic_s210;
        }

        public void setPic_s210(String pic_s210) {
            this.pic_s210 = pic_s210;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }
    }

    public static class SongListBean {
        /**
         * author : G.E.M.邓紫棋
         * all_artist_ting_uid : 7898
         * title : 泡沫
         * song_id : 14945107
         * del_status : 0
         * album_id : 13897642
         * pic_small : http://musicdata.baidu.com/data2/pic/89123644/89123644.jpg@s_0,w_90
         * resource_type : 0
         * all_artist_id : 1814
         * artist_id : 1814
         * high_rate : 320
         * all_rate : 64,128,192,256,320,flac
         * has_mv : 1
         * has_mv_mobile : 1
         * havehigh : 2
         * copy_type : 1
         * charge : 0
         * learn : 1
         * versions :
         * bitrate_fee : {"0":"0|0","1":"0|0"}
         * info : 泡沫-G.E.M.邓紫棋
         * has_filmtv : 0
         * ting_uid : 7898
         * album_title : Xposed(曝光)
         * is_first_publish : 0
         * song_source : web
         * piao_id : 0
         * korean_bb_song : 0
         * resource_type_ext : 0
         * mv_provider : 1100000000
         * artist_name : G.E.M.邓紫棋
         * rank : 2
         * biaoshi :
         */

        private String author;
        private String all_artist_ting_uid;
        private String title;
        private String song_id;
        private String del_status;
        private String album_id;
        private String pic_small;
        private String resource_type;
        private String all_artist_id;
        private String artist_id;
        private String high_rate;
        private String all_rate;
        private int has_mv;
        private int has_mv_mobile;
        private int havehigh;
        private String copy_type;
        private int charge;
        private int learn;
        private String versions;
        private String bitrate_fee;
        private String info;
        private String has_filmtv;
        private String ting_uid;
        private String album_title;
        private int is_first_publish;
        private String song_source;
        private String piao_id;
        private String korean_bb_song;
        private String resource_type_ext;
        private String mv_provider;
        private String artist_name;
        private String rank;
        private String biaoshi;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAll_artist_ting_uid() {
            return all_artist_ting_uid;
        }

        public void setAll_artist_ting_uid(String all_artist_ting_uid) {
            this.all_artist_ting_uid = all_artist_ting_uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public String getResource_type() {
            return resource_type;
        }

        public void setResource_type(String resource_type) {
            this.resource_type = resource_type;
        }

        public String getAll_artist_id() {
            return all_artist_id;
        }

        public void setAll_artist_id(String all_artist_id) {
            this.all_artist_id = all_artist_id;
        }

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getHigh_rate() {
            return high_rate;
        }

        public void setHigh_rate(String high_rate) {
            this.high_rate = high_rate;
        }

        public String getAll_rate() {
            return all_rate;
        }

        public void setAll_rate(String all_rate) {
            this.all_rate = all_rate;
        }

        public int getHas_mv() {
            return has_mv;
        }

        public void setHas_mv(int has_mv) {
            this.has_mv = has_mv;
        }

        public int getHas_mv_mobile() {
            return has_mv_mobile;
        }

        public void setHas_mv_mobile(int has_mv_mobile) {
            this.has_mv_mobile = has_mv_mobile;
        }

        public int getHavehigh() {
            return havehigh;
        }

        public void setHavehigh(int havehigh) {
            this.havehigh = havehigh;
        }

        public String getCopy_type() {
            return copy_type;
        }

        public void setCopy_type(String copy_type) {
            this.copy_type = copy_type;
        }

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public int getLearn() {
            return learn;
        }

        public void setLearn(int learn) {
            this.learn = learn;
        }

        public String getVersions() {
            return versions;
        }

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public void setBitrate_fee(String bitrate_fee) {
            this.bitrate_fee = bitrate_fee;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getHas_filmtv() {
            return has_filmtv;
        }

        public void setHas_filmtv(String has_filmtv) {
            this.has_filmtv = has_filmtv;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public void setAlbum_title(String album_title) {
            this.album_title = album_title;
        }

        public int getIs_first_publish() {
            return is_first_publish;
        }

        public void setIs_first_publish(int is_first_publish) {
            this.is_first_publish = is_first_publish;
        }

        public String getSong_source() {
            return song_source;
        }

        public void setSong_source(String song_source) {
            this.song_source = song_source;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public void setPiao_id(String piao_id) {
            this.piao_id = piao_id;
        }

        public String getKorean_bb_song() {
            return korean_bb_song;
        }

        public void setKorean_bb_song(String korean_bb_song) {
            this.korean_bb_song = korean_bb_song;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getMv_provider() {
            return mv_provider;
        }

        public void setMv_provider(String mv_provider) {
            this.mv_provider = mv_provider;
        }

        public String getArtist_name() {
            return artist_name;
        }

        public void setArtist_name(String artist_name) {
            this.artist_name = artist_name;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getBiaoshi() {
            return biaoshi;
        }

        public void setBiaoshi(String biaoshi) {
            this.biaoshi = biaoshi;
        }
    }
}
