package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wusong.common.BaseContext;
import top.wusong.common.Result;
import top.wusong.domain.User;
import top.wusong.service.UserService;
import top.wusong.utils.SMSUtils;
import top.wusong.utils.ValidateCodeUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 发送验证码
     *
     * @param user    用户的信息，这里就只有号码
     * @param session 用于存用户的号码和验证码
     * @return Result<String> 验证码是否发送成功
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        if (user != null) {
            log.info("user:{}", user);
            //获取用户的手机号码
            String phone = user.getPhone().toString();
            //获取验证码
            String code4String = ValidateCodeUtils.generateValidateCode4String(4);
            //发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code4String);
            log.info("验证码为{}", code4String);
            //存验证码，用于登录的时候验证！
            session.setAttribute(phone, code4String);
            return Result.success("验证码发送成功");
        }
        return Result.error("验证码发送失败！请稍后重试！");
    }

    /**
     * 发送验证码
     *
     * @param user    用户的信息，这里就只有号码
     * @param session 用于存用户的号码和验证码
     * @return Result<String> 验证码是否发送成功
     */
    //@PostMapping("/sendMsg")
    public Result<String> sendMsg1(@RequestBody User user, HttpSession session) {
        if (user != null) {
            log.info("user:{}", user);
            String phone = user.getPhone().toString();
            String code = sendValidateCode(phone); // 调用发送验证码的方法
            if (StringUtils.isNotEmpty(code)) {
                session.setAttribute(phone, code);
                return Result.success("验证码发送成功");
            }
        }
        return Result.error("验证码发送失败！请稍后重试！");
    }
    /**
     * 发送验证码并返回验证码
     *
     * @param phone 手机号码
     * @return 验证码
     */
    private String sendValidateCode(String phone) {
        String code4String = ValidateCodeUtils.generateValidateCode4String(4);
        //SMSUtils.sendMessage("瑞吉外卖","",phone,code4String);
        log.info("验证码为{}", code4String);
        return code4String;
    }


    //@PostMapping("/login")
    public Result<User> login1(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) { // 判断参数不为空
            String code_phone = (String) session.getAttribute(phone);
            if (StringUtils.isNotEmpty(code_phone) && code_phone.equals(code)) { // 判断验证码是否匹配
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
                if (user == null) {
                    user = new User();
                    user.setPhone(phone);
                    user.setStatus(1);
                    userService.save(user);
                }
                session.setAttribute("userid", user.getId());
                return Result.success(user);
            }
        }
        return Result.error("登录失败！");
    }

    /**
     * 用户登录
     *
     * @param map     手机号码和短信
     * @param session 存入用户的登入信息
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        log.info("code{}", map.get("code"));
        log.info("phone{}", map.get("phone"));
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        //判断用户是否存在
        String code_phone = (String) session.getAttribute(phone);
        //对比
        if (code_phone != "" && code_phone != null && code.equals(code_phone)) {
            //判断用户是否存在
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
            if (user == null) {
                user = new User();
                //不存在，保存用户
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("userid",user.getId());
            return Result.success(user);
        }
        return Result.error("登录失败！");
    }


    @PostMapping("/loginout")
    public Result<String> loginout(HttpSession session) {
        //从session删除id
        session.removeAttribute("userid");
        return Result.success("退出成功！");
    }


}
