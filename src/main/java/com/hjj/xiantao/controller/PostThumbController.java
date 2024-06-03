package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.request.posthumb.PostThumbRequest;
import com.hjj.xiantao.model.vo.PostVO;
import com.hjj.xiantao.service.PostThumbService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post_thumb")
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addPostThumb(@RequestBody PostThumbRequest postThumbRequest,
                                              HttpServletRequest request) {
        if (postThumbRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = postThumbService.addPostThumb(postThumbRequest, request);
        return ResultUtils.success(b);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePostThumb(@RequestBody PostThumbRequest postThumbRequest,
                                                 HttpServletRequest request) {
        if (postThumbRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = postThumbService.deletePostThumb(postThumbRequest, request);
        return ResultUtils.success(b);
    }

    @GetMapping("/list/my")
    public BaseResponse<List<PostVO>> listMyPostThumb(long pageNum, long pageSize, HttpServletRequest request) {
        List<PostVO> postVOList = postThumbService.listMyPostThumb(pageNum, pageSize, request);
        return ResultUtils.success(postVOList);
    }

}
