package com.oo.blog.service;

import com.oo.blog.po.Comment;

import java.util.List;

public interface CommentService {

    //获取列表
    List<Comment> listCommentByBlogId(long blogId);
    //提交数据保存
    Comment saveComment(Comment comment);

}
