package efub.back.jupjup.domain.security.userInfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    public static final String REGISTRATION_ID = "kakao";

    private static final String PROVIDER_ID = "id";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PROFILE = "profile_image_url";
    private static final String GENDER = "gender";
    private static final String AGE_RANGE = "age_range";

    private static final String KEY = "kakao_account";

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public String getProviderId() {
        return attributes.get(PROVIDER_ID).toString();
    }
    @Override
    public String getRegistrationId() {
        return REGISTRATION_ID;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get(KEY);
        return kakaoAccount.get(EMAIL).toString();
    }

    @Override
    public String getName() {
        return REGISTRATION_ID + "_" + this.getProviderId();
    }


    public String getNickname() {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get(KEY);
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
        return kakaoProfile.get(NICKNAME).toString();
    }

    @Override
    public String getProfileImageUrl(){
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get(KEY);
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
        return kakaoProfile.get(PROFILE).toString();
    }

    @Override
    public String getGender() {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get(KEY);
        return kakaoAccount.get(GENDER).toString();

    }

    @Override
    public String getAgeRange() {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get(KEY);
        return kakaoAccount.get(AGE_RANGE).toString();
    }

}
