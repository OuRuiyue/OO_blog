package com.oo.blog.dao;

import com.oo.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Long> {

    Comment findById(long Id);
    List<Comment> findByBlogIdAndParentCommentNull(long blogId, Sort sort);

}
