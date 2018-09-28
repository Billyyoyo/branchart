package cc.flyee.branchart.controllers;

import cc.flyee.branchart.exceptions.LogicException;
import cc.flyee.branchart.forms.PageQueryForm;
import cc.flyee.branchart.models.*;
import cc.flyee.branchart.services.ArticleRepository;
import cc.flyee.branchart.services.BookRepository;
import cc.flyee.branchart.services.FavoriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping(value = "/fav")
public class FavoriteController {

    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    BookRepository bookRepository;

    @PostMapping(value = "/add/{type}/{itemId}")
    public ResultEntry add(@RequestHeader(name = "ba_user_id") String userId,
                           @PathVariable Integer type,
                           @PathVariable String itemId) {
        Favorite fav = new Favorite();
        fav.setType(type);
        fav.setUserId(userId);
        fav.setItemId(itemId);
        if(type.intValue() == Favorite.TYPE_ARTICLE){
            Optional<Article> articleOptional = articleRepository.findById(itemId);
            articleOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
            Article article = articleOptional.get();
            fav.setItemTitle(article.getTitle());
            fav.setItemDescription(article.getDescription());
            fav.setOwnerId(article.getOwnerId());
            fav.setOwnerName(article.getOwnerName());
        } else {
            Optional<Book> bookOptional = bookRepository.findById(itemId);
            bookOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
            Book book = bookOptional.get();
            fav.setItemTitle(book.getTitle());
            fav.setOwnerId(book.getOwnerId());
            fav.setOwnerName(book.getOwnerName());
        }
        fav.setCreateTime(System.currentTimeMillis());
        fav.setModifyTime(System.currentTimeMillis());
        favoriteRepository.save(fav);
        return new ResultEntry(ResultCode.SUCCESS, fav);
    }

    @GetMapping(value = "/del/{favId}")
    public ResultEntry delete(@RequestHeader(name = "ba_user_id") String userId,
                              @PathVariable String favId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findById(favId);
        favoriteOptional.orElseThrow(() -> new LogicException(ResultCode.ERROR_ARTICLE_NOT_EXIST));
        Favorite fav = favoriteOptional.get();
        if (!fav.getUserId().equals(userId)) {
            throw new LogicException(ResultCode.ERROR_NO_PERMISSION);
        }
        favoriteRepository.delete(fav);
        return new ResultEntry(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/list")
    public ResultEntry list(@RequestHeader(name = "ba_user_id") String userId) {
        List<Favorite> list = favoriteRepository.findByUserId(userId);
        return new ResultEntry(ResultCode.SUCCESS, list);
    }

}
