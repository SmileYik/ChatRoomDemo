package tk.miskyle.talkroomdemo.api.group.chatter;

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
