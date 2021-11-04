package tk.miskyle.talkroomdemo.core.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Account {
  public enum Gender {
    Unknown,
    Male,
    Female;

    public static Gender valueOf(int i) {
      if (i == 0) {
        return Gender.Female;
      } else if (i == 1) {
        return Gender.Male;
      }
      return Gender.Unknown;
    }
  }

  @Getter
  private final int id;
  @Getter
  private final String email;
  @Getter
  private final String nickname;
  @Getter
  private final Gender gender;
}
