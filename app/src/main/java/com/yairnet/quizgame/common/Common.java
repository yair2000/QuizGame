package com.yairnet.quizgame.common;

import com.yairnet.quizgame.model.Question;
import com.yairnet.quizgame.model.User;

import java.util.ArrayList;
import java.util.List;

// Game progress of the current user
public class Common
{
    public static String categoryId,categoryName;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();
}