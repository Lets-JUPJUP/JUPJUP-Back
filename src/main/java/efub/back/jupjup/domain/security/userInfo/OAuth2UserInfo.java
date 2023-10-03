package efub.back.jupjup.domain.security.userInfo;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProviderId();

    String getRegistrationId();

    Map<String, Object> getAttributes();

    ProviderType getProvider();
    String getEmail();
    String getNickname();
    String getProfileImageUrl();

    String getName();

    String getGender();

    String getAgeRange();
}
