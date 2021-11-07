package tk.miskyle.talkroomdemo.api.message;

import lombok.Getter;

public enum MessageType {
  Command(0),
  Text(1),
  Image(2),
  Audio(3),
  Video(4),
  At(5);

  @Getter
  private final int id;
  MessageType(int id) {
    this.id = id;
  }

  public static MessageType getById(int id) {
    for (MessageType type : values()) {
      if (type.id == id) {
        return type;
      }
    }
    return null;
  }
}
