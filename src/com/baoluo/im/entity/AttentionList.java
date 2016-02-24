package com.baoluo.im.entity;

import java.util.Comparator;
import java.util.List;

/**
 * 我的关注
 * Created by tao.lai on 2015/9/25 0025.
 */
public class AttentionList {
    private List<Attention> Items;
    private int Count;

    public List<Attention> getItems() {
        return Items;
    }

    public void setItems(List<Attention> items) {
        Items = items;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public static class Attention implements Comparator<Attention> {

        @Override
        public int compare(Attention o1, Attention o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }

        private String Id;
        private String NickName;
        private int FollowingNum;
        private int FollowersNum;
        private int BlogNum;
        private int FollowType;
        private int Sex;
        private String Face;
        private String sortLetters;

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public int getFollowingNum() {
            return FollowingNum;
        }

        public void setFollowingNum(int followingNum) {
            FollowingNum = followingNum;
        }

        public int getFollowersNum() {
            return FollowersNum;
        }

        public void setFollowersNum(int followersNum) {
            FollowersNum = followersNum;
        }

        public int getBlogNum() {
            return BlogNum;
        }

        public void setBlogNum(int blogNum) {
            BlogNum = blogNum;
        }

        public int getFollowType() {
            return FollowType;
        }

        public void setFollowType(int followType) {
            FollowType = followType;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int sex) {
            Sex = sex;
        }

        public String getFace() {
            return Face;
        }

        public void setFace(String face) {
            Face = face;
        }
    }
}



