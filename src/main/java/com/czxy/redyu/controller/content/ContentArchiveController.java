package com.czxy.redyu.controller.content;

import com.czxy.redyu.model.dto.ArchiveDetailDTO;
import com.czxy.redyu.model.dto.ArchiveSimpleDTO;
import com.czxy.redyu.model.dto.ArchivesByYearAndMonthDTO;
import com.czxy.redyu.model.dto.SearchResultArchiveDTO;
import com.czxy.redyu.service.PostService;
import com.czxy.redyu.service.UserService;
import com.czxy.redyu.utils.QRCodeGenerator;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/14
 */
@RestController
@RequestMapping("/content/archive")
public class ContentArchiveController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    /**
     * 分页查询所有文章
     * @param pageNum 第几页
     * @param pageSize 一页多少条
     * @return
     */
    @GetMapping("/archiveForPage")
    public PageInfo<ArchiveSimpleDTO> archiveForPage(@RequestParam(defaultValue = "1", required = true) Integer pageNum,
                                                     @RequestParam(defaultValue = "5", required = true) Integer pageSize,
                                                     HttpServletRequest request) {
        return postService.archiveForPage(pageNum, pageSize);
    }

    /**
     * 根据分类名查询文章
     * @param cname
     * @return
     */
    @GetMapping("/archivesByCname")
    public PageInfo<ArchiveSimpleDTO> archivesByCname(@RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam( required = true) String cname){
        return postService.archivesByCname(cname,pageNum);
    }

    /**
     * 根据标签名查询文章
     */
    @GetMapping("/archivesByTagName")
    public PageInfo<ArchiveSimpleDTO> archivesByTname(@RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam( required = true) String tagName){
        return postService.archivesByTagName(tagName,pageNum);
    }

    /**
     * 热门文章
     *
     * @return
     */
    @GetMapping("/hotArchive")
    public List<ArchiveSimpleDTO> hotArchive() {

        return postService.hotArchive();
    }

    /**
     * 随机文章
     */
    @GetMapping("/randomArchive")
    public List<ArchiveSimpleDTO> randomArchive() {

        return postService.randomArchive();
    }

    /**
     * 访问文章  文章访问量+1
     *
     * @param url 文章路径
     * @return 文章详情
     */
    @GetMapping("/archiveByUrl/{url}")
    public ArchiveDetailDTO archiveByUrl(@PathVariable String url) {
        return postService.archiveByUrl(url);
    }

    @GetMapping("/getQCCode")
    public byte[] getQCCode(@RequestParam String text) throws IOException, WriterException {

        return QRCodeGenerator.getQRCodeImage(text, 200, 200);
    }

    @GetMapping("/search/{content}")
    public SearchResultArchiveDTO searchResult(@PathVariable String content){
        return postService.searchResult(content);
    }

    @GetMapping("/archivesByYearAndMonth")
    List<ArchivesByYearAndMonthDTO> archivesByYearAndMonth(){
        return postService.archivesByYearAndMonth();
    }

    @GetMapping("/archivesByYear/{year}")
    public PageInfo<ArchiveSimpleDTO> archiveSimpleDTOPageInfo(@PathVariable Integer year,@RequestParam Integer pageNum){
        return postService.archiveSimpleDTOPageInfo(year,pageNum);
    }

}
