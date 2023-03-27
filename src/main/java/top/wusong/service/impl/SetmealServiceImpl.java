package top.wusong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.wusong.domain.Setmeal;
import top.wusong.mapper.SetmealDao;
import top.wusong.service.SetmealService;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
}
