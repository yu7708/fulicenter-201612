package cn.ucai.fulicenter.model.bean;


import cn.ucai.fulicenter.application.I;

/**
 * Created by clawpo on 2016/10/21.
 */

public class User {

    /**
     * muserName : a952700
     * muserNick : 士大夫
     * mavatarId : 72
     * mavatarPath : user_avatar
     * mavatarSuffix : .jpg
     * mavatarType : 0
     * mavatarLastUpdateTime : 1476262984280
     */

    private String muserName;
    private String muserNick;
    private int mavatarId;
    private String mavatarPath;
    private String mavatarSuffix;
    private int mavatarType;
    private String mavatarLastUpdateTime;

    public String getMuserName() {
        return muserName;
    }

    public void setMuserName(String muserName) {
        this.muserName = muserName;
    }

    public String getMuserNick() {
        return muserNick;
    }

    public void setMuserNick(String muserNick) {
        this.muserNick = muserNick;
    }

    public int getMavatarId() {
        return mavatarId;
    }

    public void setMavatarId(int mavatarId) {
        this.mavatarId = mavatarId;
    }

    public String getMavatarPath() {
        return mavatarPath;
    }

    public void setMavatarPath(String mavatarPath) {
        this.mavatarPath = mavatarPath;
    }

    public String getMavatarSuffix() {
        return mavatarSuffix!=null?mavatarSuffix: I.AVATAR_SUFFIX_JPG;
    }

    public void setMavatarSuffix(String mavatarSuffix) {
        this.mavatarSuffix = mavatarSuffix;
    }

    public int getMavatarType() {
        return mavatarType;
    }

    public void setMavatarType(int mavatarType) {
        this.mavatarType = mavatarType;
    }

    public String getMavatarLastUpdateTime() {
        return mavatarLastUpdateTime;
    }

    public void setMavatarLastUpdateTime(String mavatarLastUpdateTime) {
        this.mavatarLastUpdateTime = mavatarLastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!getMuserName().equals(user.getMuserName())) return false;
        return getMavatarLastUpdateTime().equals(user.getMavatarLastUpdateTime());

    }

    @Override
    public int hashCode() {
        int result = getMuserName().hashCode();
        result = 31 * result + getMavatarLastUpdateTime().hashCode();
        return result;
    }
    //downloadAvatar?name_or_hxid=gsd123&avatarType=user_avatar&m_avatar_suffix=.jpg&width=80&height=80
    public String getAvatar(){
    //头像下载的拼接
        StringBuilder sb=new StringBuilder(I.DOWNLOAD_AVATAR_URL);
        sb.append(I.NAME_OR_HXID).append(I.EQUAL).append(this.getMuserName())
                .append(I.AND).append(I.AVATAR_TYPE).append(I.EQUAL).append(this.getMavatarType())
                .append(I.AND).append(I.AVATAR_SUFFIX).append(I.EQUAL).append(this.getMavatarSuffix())
                .append("&width=200&height=200");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "muserName='" + muserName + '\'' +
                ", muserNick='" + muserNick + '\'' +
                ", mavatarId=" + mavatarId +
                ", mavatarPath='" + mavatarPath + '\'' +
                ", mavatarSuffix='" + mavatarSuffix + '\'' +
                ", mavatarType=" + mavatarType +
                ", mavatarLastUpdateTime='" + mavatarLastUpdateTime + '\'' +
                '}';
    }
}
