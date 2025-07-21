package com.example.moarefiprod.data

import com.example.moarefiprod.R // برای دسترسی به R.drawable
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.Course
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseItem
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseItemType
import com.example.moarefiprod.ui.theme.FourPageAsli.CommonMain.courspage.CourseLesson

// این لیست شامل تمام دوره‌ها با تمام جزئیات (از جمله دروس و آیتم‌های درون دروس) است.
// شما باید مقادیر دروس (lessons) و آیتم‌های آن‌ها (items) را با داده‌های واقعی خود پر کنید.
val allAppCourses = listOf(
    Course(
        title = "A1 آموزش آلمانی سطح",
        description = "با این دوره، می‌توانید به راحتی آلمانی را یاد بگیرید!",
        sath = "A1",
        zaman = "۱۰ ساعت و ۳۰ دقیقه",
        teadad = "۱۲ جلسه + ۲۴ آزمون",
        price = 120, // قیمت به واحد مورد نظر شما
        image = R.drawable.cours1,
        isNew = false,
        isPurchased = false,
        lessons = listOf(
            CourseLesson(
                id = "01", // ID یونیک برای این درس در این دوره
                title = "الفبای آلمانی و تلفظ حروف",
                duration = "15 دقیقه", // مدت زمان به دقیقه
                isUnlocked = true, // فرض کنید این درس باز است
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.VIDEO, "فیلم آموزشی الفبا", isCompleted = true),
                    CourseItem(CourseItemType.DOCUMENT, "جزوه الفبا و تمرین", isCompleted = false, isInProgress = true),
                    CourseItem(CourseItemType.QUIZ1, "آزمون اولیه افعال Modal", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "02",
                title = "گرامر پایه A1 (افعال Modal)",
                duration = "20 دقیقه",
                isUnlocked = true,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.VIDEO, "معرفی افعال Modal", isCompleted = false),
                    CourseItem(CourseItemType.QUIZ1, "آزمون اولیه افعال Modal", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "03",
                title = "مکالمه ساده و معرفی خود",
                duration = "25 دقیقه",
                isUnlocked = true,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.VIDEO, "مکالمه معرفی", isCompleted = false),
                    CourseItem(CourseItemType.WORDS, "کلمات رایج معرفی", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "04",
                title = "آزمون جامع درس‌های 1 تا 3",
                duration = "30 دقیقه",
                isUnlocked = true,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.FINAL_EXAM, "آزمون نهایی بخش اول A1", isCompleted = false)
                )
            )
            // ... بقیه درس‌های مربوط به دوره A1 را اینجا اضافه کنید
        )
    ),
    Course(
        title = "A2 آموزش آلمانی سطح",
        description = "ادامه مسیر یادگیری آلمانی با نکات بیشتر",
        sath = "A2",
        zaman = "۹ ساعت",
        teadad = "۱۰ جلسه + تمرین",
        price = 0, // دوره رایگان
        image = R.drawable.cours1,
        isNew = false,
        isPurchased = false,
        lessons = listOf(
            CourseLesson(
                id = "01",
                title = "مکالمه روزمره A2 (خرید و فروش)",
                duration = "25 دقیقه",
                isUnlocked = true,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.VIDEO, "فیلم مکالمه در فروشگاه", isCompleted = false),
                    CourseItem(CourseItemType.WORDS, "لغات مربوط به خرید", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "02",
                title = "گرامر پیشرفته A2 (حالت داتیو و آکوزاتیو)",
                duration = "30 دقیقه",
                isUnlocked = true,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.DOCUMENT, "جزوه جامع داتیو و آکوزاتیو", isCompleted = false),
                    CourseItem(CourseItemType.QUIZ2, "آزمون تخصصی داتیو/آکوزاتیو", isCompleted = false)
                )
            )
            // ... بقیه درس‌های مربوط به دوره A2 را اینجا اضافه کنید
        )
    ),
    Course(
        title = "B1 آموزش آلمانی سطح",
        description = "آمادگی برای مکالمه‌های روزمره و آزمون‌ها",
        sath = "B1",
        zaman = "۱۱ ساعت",
        teadad = "۱۴ جلسه + پروژه",
        price = 200,
        image = R.drawable.cours1,
        isNew = true, // این دوره جدید است
        isPurchased = false,
        lessons = listOf(
            CourseLesson(
                id = "01",
                title = "گرامر B1 (Adjektivdeklination)",
                duration = " دقیقه 40",
                isUnlocked = false, // قفل است تا خریداری شود
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.VIDEO, "آموزش Adjektivdeklination", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "02",
                title = "مکالمه B1 (مذاکره و بحث)",
                duration = " دقیقه 35",
                isUnlocked = false,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.VIDEO, "نمونه مکالمه‌های B1", isCompleted = false)
                )
            )
            // ... بقیه درس‌های مربوط به دوره B1 را اینجا اضافه کنید
        )
    ),
    Course(
        title = "B2 آموزش آلمانی سطح", // اولین دوره B2
        description = "مکالمه روان و درک عمیق‌تر",
        sath = "B2",
        zaman = "۱۳ ساعت",
        teadad = "۱۵ جلسه + تمرین تعاملی",
        price = 250,
        image = R.drawable.cours1,
        isNew = false,
        isPurchased = false,
        lessons = listOf(
            CourseLesson(
                id = "01",
                title = "لغات و اصطلاحات تخصصی B2",
                duration = "50 دقیقه",
                isUnlocked = false,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.WORDS, "واژگان تخصصی", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "02",
                title = "فهم متون پیچیده B2",
                duration = "45 دقیقه",
                isUnlocked = false,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.DOCUMENT, "متون علمی و ادبی", isCompleted = false)
                )
            )
            // ... بقیه درس‌های مربوط به اولین دوره B2 را اینجا اضافه کنید
        )
    ),
    Course(
        title = "B2 آموزش آلمانی سطح", // دومین دوره B2 (اگر واقعاً متفاوت است)
        description = "تمرینات تعاملی پیشرفته B2",
        sath = "B2",
        zaman = "۱۴ ساعت",
        teadad = "۱۶ جلسه + پروژه‌های عملی",
        price = 250,
        image = R.drawable.cours1,
        isNew = true, // این دوره B2 جدید است
        isPurchased = false,
        lessons = listOf(
            CourseLesson(
                id = "01",
                title = "پروژه نوشتاری B2",
                duration = "60 دقیقه",
                isUnlocked = false,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.QUIZ3, "تمرین نوشتاری", isCompleted = false)
                )
            ),
            CourseLesson(
                id = "02",
                title = "آمادگی آزمون B2",
                duration = "70 دقیقه",
                isUnlocked = false,
                isCompleted = false,
                items = listOf(
                    CourseItem(CourseItemType.FINAL_EXAM, "شبیه‌ساز آزمون B2", isCompleted = false)
                )
            )
            // ... بقیه درس‌های مربوط به دومین دوره B2 را اینجا اضافه کنید
        )
    )
)