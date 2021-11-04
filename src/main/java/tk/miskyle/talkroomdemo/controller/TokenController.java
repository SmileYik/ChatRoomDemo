package tk.miskyle.talkroomdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.miskyle.talkroomdemo.core.token.Account;
import tk.miskyle.talkroomdemo.core.token.TokenManager;

@RestController
@RequestMapping("/token")
public class TokenController {
  @RequestMapping("/register")
  public String register(@RequestParam(value = "id") int id,
                       @RequestParam(value = "email") String email,
                       @RequestParam(value = "nickname") String nickname,
                       @RequestParam(value = "gender") int gender) {
    if (TokenManager.containsValue(id)) {
      // TODO Exit And Retry To JOIN
      return null;
    }
    return TokenManager.put(new Account(id, email, nickname, Account.Gender.valueOf(gender)));
  }
}
