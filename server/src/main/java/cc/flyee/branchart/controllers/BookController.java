package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.EditBookForm;
import cc.flyee.branchart.forms.PageQueryForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.ArticleRepository;
import cc.flyee.branchart.services.BookRepository;
import cc.flyee.branchart.services.UserExtraRepository;
import cc.flyee.branchart.services.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserExtraRepository userExtraRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MessageSource messageSource;

    /**
     * 创建book   只创建profile节点
     *
     * @param userId
     * @param form
     * @return
     */
    @PostMapping(value = "/create")
    public ResultEntry create(@RequestHeader(name = "ba_user_id") String userId,
                              @RequestBody EditBookForm form) {
        User user = userRepository.findById(userId).get();
        UserExtra userExtra = userExtraRepository.findById(user.getExtraId()).get();
        Optional<String> titleOptional = Optional.ofNullable(form.getTitle());
        String title = titleOptional.orElse(messageSource.getMessage("text.some_one_publish", null, LocaleContextHolder.getLocale()));
        Book book = new Book();
        book.setTitle(title);
        book.setType(Book.TYPE_PROFILE);
        book.setNum(0);
        book.setOwnerId(user.getId());
        book.setOwnerName(user.getNickName());
        book.setCreateTime(System.currentTimeMillis());
        book.setModifyTime(System.currentTimeMillis());
        bookRepository.save(book);
        userExtra.setBookCount(userExtra.getBookCount() + 1);
        userExtraRepository.save(userExtra);
        return new ResultEntry(ResultCode.SUCCESS, book);
    }

    @PostMapping(value = "/update/{bookId}")
    public ResultEntry update(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable String bookId,
                              @RequestBody EditBookForm form) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        bookOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Book book = bookOptional.get();
        if (!book.getOwnerId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        book.setTitle(form.getTitle());
        book.setModifyTime(System.currentTimeMillis());
        bookRepository.save(book);
        return new ResultEntry(ResultCode.SUCCESS, book);
    }

    @GetMapping(value = "/{bookId}")
    public ResultEntry get(@PathVariable String bookId,
                           @ModelAttribute PageQueryForm queryForm) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        bookOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Book book = bookOptional.get();
        PageModel pager = queryForm.getPager();
        pager.setSort(new Sort(Sort.Direction.ASC, "num"));
        List<Book> chapters = bookRepository.findByProfileIdAndType(bookId, Book.TYPE_CHAPTER, pager);
        return new ResultEntry(ResultCode.SUCCESS, ResultData.build().put("book", book).put("chapters", chapters).getData());
    }

    @GetMapping(value = "/list/{userId}")
    public ResultEntry list(@PathVariable String userId,
                            @ModelAttribute PageQueryForm pageForm) {
        List<Book> books = bookRepository.findByOwnerIdAndType(userId, Book.TYPE_PROFILE, pageForm.getPager());
        return new ResultEntry(ResultCode.SUCCESS, books);
    }

    @GetMapping(value = "/list")
    public ResultEntry list(@ModelAttribute PageQueryForm pageForm) {
        PageModel pager = pageForm.getPager();
        pager.setSort(new Sort(Sort.Direction.DESC, "modifyTime"));
        List<Book> books = bookRepository.findByType(Book.TYPE_PROFILE, pager);
        return new ResultEntry(ResultCode.SUCCESS, books);
    }

    @PostMapping(value = "/addarticle/{bookId}/{articleId}")
    public ResultEntry addArticle(@RequestHeader(name = "ba_user_id") String userId,
                                  @PathVariable String bookId,
                                  @PathVariable String articleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        bookOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Book profile = bookOptional.get();
        if (!profile.getOwnerId().equals(user.getId())) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        Article article = articleRepository.findById(articleId).get();
        Book chapter = new Book();
        chapter.setDescription(article.getDescription());
        chapter.setOwnerId(article.getOwnerId());
        chapter.setOwnerName(article.getOwnerName());
        chapter.setArticleId(articleId);
        chapter.setType(Book.TYPE_CHAPTER);
        chapter.setCreateTime(System.currentTimeMillis());
        chapter.setModifyTime(System.currentTimeMillis());
        chapter.setProfileId(profile.getId());
        chapter.setNum(profile.getNum() + 1);
        bookRepository.save(chapter);
        profile.setNum(profile.getNum() + 1);
        bookRepository.save(profile);
        return new ResultEntry(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/delarticle/{chapterId}")
    public ResultEntry delArticle(@RequestHeader(name = "ba_user_id") String userId,
                                  @PathVariable String chapterId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        Optional<Book> chapterOptional = bookRepository.findById(chapterId);
        chapterOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Book chapter = chapterOptional.get();
        Optional<Book> profileOptional = bookRepository.findById(chapter.getProfileId());
        Book profile = profileOptional.get();
        if (!profile.getOwnerId().equals(user.getId())) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        bookRepository.deleteById(chapterId);
        profile.setNum(profile.getNum() - 1);
        bookRepository.save(profile);
        return new ResultEntry(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/authers/{bookId}")
    public ResultEntry getAuthers(@PathVariable String bookId) {
        throw new LogicException(ResultCode.ERROR_INTERNAL);
    }

    @GetMapping(value = "/autherchapters/{bookId}/{userId}")
    public ResultEntry getAutherChapters(@PathVariable String bookId,
                                         @PathVariable String userId) {
        throw new LogicException(ResultCode.ERROR_INTERNAL);
    }

}
