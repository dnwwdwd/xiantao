package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.request.postfavour.PostFavourRequest;
import com.hjj.xiantao.model.vo.PostVO;
import com.hjj.xiantao.service.PostFavourService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post_favour")
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addPostFavour(@RequestBody PostFavourRequest postFavourRequest,
                                               HttpServletRequest request) {
        if (postFavourRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = postFavourService.addPostFavour(postFavourRequest, request);
        return ResultUtils.success(b);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePostFavour(@RequestBody PostFavourRequest postFavourRequest,
                                               HttpServletRequest request) {
        if (postFavourRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = postFavourService.deletePostFavour(postFavourRequest, request);
        return ResultUtils.success(b);
    }

    @GetMapping("/list/my")
    public BaseResponse<List<PostVO>> listMyPostFavour(long pageNum, long pageSize,
                                                      HttpServletRequest request) {
        List<PostVO> postVOList = postFavourService.listMyPostFavour(pageNum, pageSize, request);
        return ResultUtils.success(postVOList);
    }
}
