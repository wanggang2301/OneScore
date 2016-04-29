package com.hhly.mlottery.bean.videobean;

import java.util.List;

/**
 * Created by 103TJL on 2016/1/8.
 * 直播json实体类
 */
public class MatchVideoInfo {
 private String fix;//图片后缀
 private String preurl;//图片地址前缀


   /**
    * date : 2016-01-19
    * sptVideoInfoDtoList : [{"matchId":"273551","hmId":"13279","awId":"20032","hometeam":"河内","guestteam":"广宁","matchDate":"2016-01-19","matchTime":"16:00","racename":"球会友谊","round":"","statusOrigin":"1","channel":[{"name":"QQ直播","url":"http://m.13322.com"}]},{"matchId":"273552","hmId":"9824","awId":"9826","hometeam":"大南城主","guestteam":"海防水泥队","matchDate":"2016-01-19","matchTime":"18:00","racename":"球会友谊","round":"","statusOrigin":"0","channel":[{"name":"QQ直播","url":"http://m.13322.com"}]},{"matchId":"275012","hmId":"1143","awId":"4458","hometeam":"萨比利斯","guestteam":"马特斯","matchDate":"2016-01-19","matchTime":"19:00","racename":"球会友谊","round":"","statusOrigin":"0","channel":[{"name":"QQ直播","url":"http://m.13322.com"}]},{"matchId":"272795","hmId":"6364","awId":"2533","hometeam":"利夫尼斯","guestteam":"伊赫拉瓦","matchDate":"2016-01-19","matchTime":"20:00","racename":"TIP杯","round":"分组赛","statusOrigin":"0","channel":[{"name":"QQ直播","url":"http://m.13322.com"}]}]
    */

   private List<MatchVideoEntity> matchVideo;

   public void setMatchVideo(List<MatchVideoEntity> matchVideo) {
      this.matchVideo = matchVideo;
   }

   public List<MatchVideoEntity> getMatchVideo() {
      return matchVideo;
   }

   public String getFix() {
      return fix;
   }

   public void setFix(String fix) {
      this.fix = fix;
   }

   public String getPreurl() {
      return preurl;
   }

   public void setPreurl(String preurl) {
      this.preurl = preurl;
   }

   public static class MatchVideoEntity {
      private String date;
      /**
       * matchId : 273551
       * hmId : 13279
       * awId : 20032
       * hometeam : 河内
       * guestteam : 广宁
       * matchDate : 2016-01-19
       * matchTime : 16:00
       * racename : 球会友谊
       * round :
       * statusOrigin : 1
       * channel : [{"name":"QQ直播","url":"http://m.13322.com"}]
       */

      private List<SptVideoInfoDtoListEntity> sptVideoInfoDtoList;

      public void setDate(String date) {
         this.date = date;
      }

      public void setSptVideoInfoDtoList(List<SptVideoInfoDtoListEntity> sptVideoInfoDtoList) {
         this.sptVideoInfoDtoList = sptVideoInfoDtoList;
      }

      public String getDate() {
         return date;
      }

      public List<SptVideoInfoDtoListEntity> getSptVideoInfoDtoList() {
         return sptVideoInfoDtoList;
      }

      public static class SptVideoInfoDtoListEntity {
         private String matchId;
         private String hmId;
         private String awId;
         private String hometeam;
         private String guestteam;
         private String matchDate;
         private String matchTime;
         private String racename;
         private String round;
         private String statusOrigin;
         /**
          * name : QQ直播
          * url : http://m.13322.com
          */

         private List<ChannelEntity> channel;

         public void setMatchId(String matchId) {
            this.matchId = matchId;
         }

         public void setHmId(String hmId) {
            this.hmId = hmId;
         }

         public void setAwId(String awId) {
            this.awId = awId;
         }

         public void setHometeam(String hometeam) {
            this.hometeam = hometeam;
         }

         public void setGuestteam(String guestteam) {
            this.guestteam = guestteam;
         }

         public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
         }

         public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
         }

         public void setRacename(String racename) {
            this.racename = racename;
         }

         public void setRound(String round) {
            this.round = round;
         }

         public void setStatusOrigin(String statusOrigin) {
            this.statusOrigin = statusOrigin;
         }

         public void setChannel(List<ChannelEntity> channel) {
            this.channel = channel;
         }

         public String getMatchId() {
            return matchId;
         }

         public String getHmId() {
            return hmId;
         }

         public String getAwId() {
            return awId;
         }

         public String getHometeam() {
            return hometeam;
         }

         public String getGuestteam() {
            return guestteam;
         }

         public String getMatchDate() {
            return matchDate;
         }

         public String getMatchTime() {
            return matchTime;
         }

         public String getRacename() {
            return racename;
         }

         public String getRound() {
            return round;
         }

         public String getStatusOrigin() {
            return statusOrigin;
         }

         public List<ChannelEntity> getChannel() {
            return channel;
         }

         public static class ChannelEntity {
            private String name;
            private String url;

            public void setName(String name) {
               this.name = name;
            }

            public void setUrl(String url) {
               this.url = url;
            }

            public String getName() {
               return name;
            }

            public String getUrl() {
               return url;
            }
         }
      }
   }
}
