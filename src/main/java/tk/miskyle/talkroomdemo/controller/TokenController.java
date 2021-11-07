package tk.miskyle.talkroomdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.miskyle.talkroomdemo.api.group.chatter.Gender;
import tk.miskyle.talkroomdemo.core.group.GroupManager;
import tk.miskyle.talkroomdemo.core.token.Account;
import tk.miskyle.talkroomdemo.core.token.TokenManager;
import tk.miskyle.talkroomdemo.core.util.MapBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {
  public static final int JOIN_GROUP_RESULT_NO_TOKEN = 1;
  public static final int JOIN_GROUP_RESULT_NO_GROUP = 2;

  @RequestMapping("/register")
  public String register(@RequestParam(value = "id") int id,
                       @RequestParam(value = "email") String email,
                       @RequestParam(value = "nickname") String nickname,
                       @RequestParam(value = "gender") int gender) {
    if (TokenManager.containsValue(id)) {
      // TODO Exit And Retry To JOIN
      return null;
    }
    return TokenManager.put(new Account(id, email, nickname, Gender.valueOf(gender)));
  }

  @RequestMapping("/joinGroup")
  public Map<String, Object> joinGroup(@RequestParam("token") String token,
                                       @RequestParam("group") String group) {
    if (!TokenManager.containsKey(token)) {
      System.out.println(token);
      return new MapBuilder<String, Object>()
              .append("code", JOIN_GROUP_RESULT_NO_TOKEN)
              .append("msg", "你未出示你的凭据或者凭据无效.").getMap();
    } else if (!GroupManager.containsGroup(group)) {
      return new MapBuilder<String, Object>()
              .append("code", JOIN_GROUP_RESULT_NO_GROUP)
              .append("msg", "所选定的组别无效.").getMap();
    }
    return GroupManager.getGroup(group).connect(token);
  }
}
