export interface LessonItem {
  korean: string;
  romanization: string;
  english: string;
  urdu: string;
  exampleKorean: string;
  exampleEnglish: string;
  exampleUrdu: string;
}

export interface QuizQuestion {
  question: string;
  urduQuestion: string;
  options: string[];
  correctIndex: number;
  explanation: string;
}

export interface Lesson {
  id: string;
  title: string;
  koreanTitle: string;
  urduTitle: string;
  description: string;
  grammarNotes: string;
  urduExplanation: string;
  items: LessonItem[];
  quiz: QuizQuestion[];
}

export interface Level {
  id: string;
  name: string;
  koreanName: string;
  urduName: string;
  description: string;
  lessons: Lesson[];
}

export interface DictionaryEntry {
  korean: string;
  romanization: string;
  english: string;
  urdu: string;
  category: string;
  level: string;
}

export interface HangulChar {
  char: string;
  name: string;
  sound: string;
  urduSound: string;
  type: 'Consonant' | 'Vowel';
}

export const HANGUL_ALPHABET: HangulChar[] = [
  { char: 'ㄱ', name: 'Giyeok', sound: 'g/k', urduSound: 'گ یا ک', type: 'Consonant' },
  { char: 'ㄴ', name: 'Nieun', sound: 'n', urduSound: 'ن', type: 'Consonant' },
  { char: 'ㄷ', name: 'Digeut', sound: 'd/t', urduSound: 'د یا ت', type: 'Consonant' },
  { char: 'ㄹ', name: 'Rieul', sound: 'r/l', urduSound: 'ر یا ل', type: 'Consonant' },
  { char: 'ㅁ', name: 'Mieum', sound: 'm', urduSound: 'م', type: 'Consonant' },
  { char: 'ㅂ', name: 'Bieup', sound: 'b/p', urduSound: 'ب یا پ', type: 'Consonant' },
  { char: 'ㅅ', name: 'Siot', sound: 's', urduSound: 'س', type: 'Consonant' },
  { char: 'ㅇ', name: 'Ieung', sound: '- / ng', urduSound: 'خاموش / نگ', type: 'Consonant' },
  { char: 'ㅈ', name: 'Jieut', sound: 'j', urduSound: 'ج', type: 'Consonant' },
  { char: 'ㅊ', name: 'Chieut', sound: 'ch', urduSound: 'چ', type: 'Consonant' },
  { char: 'ㅋ', name: 'Kieuk', sound: 'k', urduSound: 'کھ', type: 'Consonant' },
  { char: 'ㅌ', name: 'Tieut', sound: 't', urduSound: 'تھ', type: 'Consonant' },
  { char: 'ㅍ', name: 'Pieup', sound: 'p', urduSound: 'پھ', type: 'Consonant' },
  { char: 'ㅎ', name: 'Hieut', sound: 'h', urduSound: 'ہ', type: 'Consonant' },
  { char: 'ㅏ', name: 'a', sound: 'ah', urduSound: 'آ', type: 'Vowel' },
  { char: 'ㅑ', name: 'ya', sound: 'yah', urduSound: 'یا', type: 'Vowel' },
  { char: 'ㅓ', name: 'eo', sound: 'uh', urduSound: 'اَ', type: 'Vowel' },
  { char: 'ㅕ', name: 'yeo', sound: 'yuh', urduSound: 'یَ', type: 'Vowel' },
  { char: 'ㅗ', name: 'o', sound: 'oh', urduSound: 'او', type: 'Vowel' },
  { char: 'ㅛ', name: 'yo', sound: 'yoh', urduSound: 'یو', type: 'Vowel' },
  { char: 'ㅜ', name: 'u', sound: 'oo', urduSound: 'اُو', type: 'Vowel' },
  { char: 'ㅠ', name: 'yu', sound: 'yoo', urduSound: 'یُو', type: 'Vowel' },
  { char: 'ㅡ', name: 'eu', sound: 'eu', urduSound: 'اِو', type: 'Vowel' },
  { char: 'ㅣ', name: 'i', sound: 'ee', urduSound: 'ای', type: 'Vowel' },
];

export const KOREAN_LEVELS: Level[] = [
  {
    id: 'beginner',
    name: 'Beginner',
    koreanName: '초급',
    urduName: 'ابتدائی سطح',
    description: 'Hangul alphabet, consonants, vowels, syllable blocks, basic greetings, numbers and everyday vocabulary.',
    lessons: [
      {
        id: 'beg_01',
        title: 'Hangul Alphabet: Basic Consonants',
        koreanTitle: '한글 기본 자음',
        urduTitle: 'ہنگول حروفِ تہجی: بنیادی حروفِ صامت',
        description: 'Master the 14 core consonants designed by King Sejong based on speech organs.',
        grammarNotes: 'Consonants are formed based on the shape of the mouth, tongue, and throat. For example, ㄱ mimics the tongue root blocking the throat, while ㄴ shows the tongue tip touching the alveolar ridge.',
        urduExplanation: 'کورین زبان کے حروفِ تہجی (ہنگول) 1443 میں شاہ سیجونگ نے ایجاد کیے۔ بنیادی 14 حروفِ صامت بولتے وقت زبان اور منہ کی حرکات کی عکاسی کرتے ہیں۔',
        items: [
          {
            korean: 'ㄱ',
            romanization: 'Giyeok (g/k)',
            english: 'G sound at start / K at end',
            urdu: 'گ یا ک کی آواز',
            exampleKorean: '가방 (gabang)',
            exampleEnglish: 'Bag / Backpack',
            exampleUrdu: 'بستہ / بیگ'
          },
          {
            korean: 'ㄴ',
            romanization: 'Nieun (n)',
            english: 'N as in nose',
            urdu: 'ن کی آواز',
            exampleKorean: '나무 (namu)',
            exampleEnglish: 'Tree / Wood',
            exampleUrdu: 'درخت'
          },
          {
            korean: 'ㄷ',
            romanization: 'Digeut (d/t)',
            english: 'D sound at start / T at end',
            urdu: 'د یا ت کی آواز',
            exampleKorean: '다리 (dari)',
            exampleEnglish: 'Bridge / Leg',
            exampleUrdu: 'پل / ٹانگ'
          },
          {
            korean: 'ㄹ',
            romanization: 'Rieul (r/l)',
            english: 'Soft R between vowels / L at end',
            urdu: 'ر یا ل کی درمیانی نرم آواز',
            exampleKorean: '라면 (ramyeon)',
            exampleEnglish: 'Ramen noodles',
            exampleUrdu: 'رامیون نوڈلز'
          },
          {
            korean: 'ㅁ',
            romanization: 'Mieum (m)',
            english: 'M as in moon',
            urdu: 'م کی آواز',
            exampleKorean: '모자 (moja)',
            exampleEnglish: 'Hat / Cap',
            exampleUrdu: 'ٹوپی'
          },
          {
            korean: 'ㅂ',
            romanization: 'Bieup (b/p)',
            english: 'B sound at start / P at end',
            urdu: 'ب یا پ کی آواز',
            exampleKorean: '바나나 (banana)',
            exampleEnglish: 'Banana',
            exampleUrdu: 'کیلا'
          },
          {
            korean: 'ㅅ',
            romanization: 'Siot (s)',
            english: 'S as in sun (Sh before i/y)',
            urdu: 'س کی آواز',
            exampleKorean: '사과 (sagwa)',
            exampleEnglish: 'Apple',
            exampleUrdu: 'سیب'
          },
          {
            korean: 'ㅇ',
            romanization: 'Ieung (silent / ng)',
            english: 'Silent initial placeholder, "ng" as final consonant',
            urdu: 'شروع میں خاموش، آخر میں غنہ ن (نگ)',
            exampleKorean: '아이 (ai)',
            exampleEnglish: 'Child / Kid',
            exampleUrdu: 'بچہ'
          }
        ],
        quiz: [
          {
            question: 'Which Hangul consonant represents the sound "N" as in nose?',
            urduQuestion: 'کون سا کورین حرف "ن" کی آواز دیتا ہے؟',
            options: ['ㄱ', 'ㄴ', 'ㅁ', 'ㅅ'],
            correctIndex: 1,
            explanation: 'ㄴ (Nieun) represents the "N" sound in Korean.'
          },
          {
            question: 'What is the meaning of the Korean word "사과" (sagwa)?',
            urduQuestion: '"사과" کا کیا مطلب ہے؟',
            options: ['Apple', 'Tree', 'Hat', 'Bag'],
            correctIndex: 0,
            explanation: '사과 (sagwa) translates to Apple (سیب).'
          }
        ]
      },
      {
        id: 'beg_02',
        title: 'Essential Korean Greetings & Etiquette',
        koreanTitle: '기본 인사 및 예절',
        urduTitle: 'بنیادی سلام اور رسمی آداب',
        description: 'Polite greetings used every single day in South Korea with appropriate bow angles.',
        grammarNotes: 'Polite speech in Korean frequently concludes with -요 (-yo) or the formal -습니다 (-seumnida). Politeness is essential when talking to seniors, strangers, or colleagues.',
        urduExplanation: 'کورین کلچر میں احترام اور آداب کو اولین اہمیت حاصل ہے۔ سلام کرتے وقت سر جھکا کر تسلیمات پیش کی جاتی ہیں اور گفتگو میں احترامی الفاظ شامل کیے جاتے ہیں۔',
        items: [
          {
            korean: '안녕하세요',
            romanization: 'Annyeonghaseyo',
            english: 'Hello / Good morning / How are you?',
            urdu: 'ہیلو / السلام علیکم',
            exampleKorean: '안녕하세요, 저는 샤합이에요.',
            exampleEnglish: 'Hello, I am Shahab.',
            exampleUrdu: 'السلام علیکم، میں شہاب ہوں۔'
          },
          {
            korean: '감사합니다',
            romanization: 'Gamsahamnida',
            english: 'Thank you (Formal polite)',
            urdu: 'آپ کا بہت شکریہ',
            exampleKorean: '도와주셔서 감사합니다.',
            exampleEnglish: 'Thank you for helping me.',
            exampleUrdu: 'مدد کرنے کا بہت شکریہ۔'
          },
          {
            korean: '죄송합니다',
            romanization: 'Joesonghamnida',
            english: 'I am sorry / Excuse me',
            urdu: 'معاف کیجیے گا / معذرت خواہ ہوں',
            exampleKorean: '늦어서 죄송합니다.',
            exampleEnglish: 'I am sorry for being late.',
            exampleUrdu: 'دیر سے آنے پر معذرت خواہ ہوں۔'
          },
          {
            korean: '안녕히 계세요',
            romanization: 'Annyeonghi gyeseyo',
            english: 'Goodbye (When you leave and the other person stays: "Stay peacefully")',
            urdu: 'خدا حافظ (جب آپ جا رہے ہوں اور میزبان وہیں رہے)',
            exampleKorean: '먼저 갑니다. 안녕히 계세요!',
            exampleEnglish: 'I am leaving first. Goodbye!',
            exampleUrdu: 'میں پہلے نکلتا ہوں، اللہ نگہبان!'
          },
          {
            korean: '안녕히 가세요',
            romanization: 'Annyeonghi gaseyo',
            english: 'Goodbye (When the other person is leaving: "Go peacefully")',
            urdu: 'فی امان اللہ (جب دوسرا شخص رخصت ہو رہا ہو)',
            exampleKorean: '조심히 가세요. 안녕히 가세요.',
            exampleEnglish: 'Travel safely. Goodbye.',
            exampleUrdu: 'حفاظت سے جائیں، خدا حافظ۔'
          },
          {
            korean: '만나서 반갑습니다',
            romanization: 'Mannaseo bangapseumnida',
            english: 'Nice to meet you (Formal)',
            urdu: 'آپ سے مل کر بہت خوشی ہوئی',
            exampleKorean: '처음 뵙겠습니다. 반갑습니다.',
            exampleEnglish: 'How do you do. Nice to meet you.',
            exampleUrdu: 'پہلی بار مل رہے ہیں، خوشی ہوئی۔'
          }
        ],
        quiz: [
          {
            question: 'When YOU are leaving a shop or home, what goodbye do you say to the person staying?',
            urduQuestion: 'جب آپ دکان یا گھر سے رخصت ہو رہے ہوں اور سامنے والا وہیں رہے تو کیا کہیں گے؟',
            options: ['안녕히 가세요', '안녕히 계세요', '감사합니다', '죄송합니다'],
            correctIndex: 1,
            explanation: '안녕히 계세요 means "Stay peacefully" (رہنے والے کو کہا جاتا ہے).'
          }
        ]
      },
      {
        id: 'beg_03',
        title: 'Korean Numbers: Sino & Native Systems',
        koreanTitle: '한국어 숫자 체계',
        urduTitle: 'کورین اعداد کے دونوں نظام',
        description: 'Distinguish when to use Sino-Korean (currency, dates, minutes) vs Native Korean (objects, age, hours).',
        grammarNotes: 'Sino-Korean numbers: 일, 이, 삼, 사, 오, 육, 칠, 팔, 구, 십. Native Korean: 하나, 둘, 셋, 넷, 다섯, 여섯, 일곱, 여덟, 아홉, 열. Native numbers change form before counters: 한 개, 두 개, 세 개.',
        urduExplanation: 'کورین میں گنتی کے 2 طریقے ہیں: سائنو کورین (پیسے، تاریخ اور منٹ کے لیے) اور مقامی کورین (چیزیں گننے، عمر اور گھنٹوں کے لیے)۔',
        items: [
          {
            korean: '일 / 하나',
            romanization: 'Il / Hana',
            english: 'One (Sino / Native)',
            urdu: 'ایک (سائنو / مقامی)',
            exampleKorean: '사과 하나 주세요.',
            exampleEnglish: 'Please give me one apple.',
            exampleUrdu: 'براہ کرم مجھے ایک سیب دیں۔'
          },
          {
            korean: '이 / 둘',
            romanization: 'I / Dul',
            english: 'Two (Sino / Native)',
            urdu: 'دو (سائنو / مقامی)',
            exampleKorean: '커피 두 잔 주세요.',
            exampleEnglish: 'Two cups of coffee, please.',
            exampleUrdu: 'دو کپ کافی دیجیے۔'
          },
          {
            korean: '삼 / 셋',
            romanization: 'Sam / Set',
            english: 'Three (Sino / Native)',
            urdu: 'تین (سائنو / مقامی)',
            exampleKorean: '삼월 (Sam-wol)',
            exampleEnglish: 'March (Month 3)',
            exampleUrdu: 'مارچ (تیسرا مہینہ)'
          },
          {
            korean: '백 / 천 / 만',
            romanization: 'Baek / Cheon / Man',
            english: '100 / 1,000 / 10,000 (Won currency)',
            urdu: 'سو / ہزار / دس ہزار (وان)',
            exampleKorean: '만 원 (Man won)',
            exampleEnglish: '10,000 Won (~$7.50)',
            exampleUrdu: 'دس ہزار وان'
          }
        ],
        quiz: [
          {
            question: 'Which number system is used for Korean Won currency and months of the year?',
            urduQuestion: 'کورین کرنسی (وان) اور سال کے مہینوں کے لیے کون سی گنتی استعمال ہوتی ہے؟',
            options: ['Native Korean', 'Sino-Korean', 'Roman numerals', 'Latin numbers'],
            correctIndex: 1,
            explanation: 'Sino-Korean (일, 이, 삼...) is used for prices, money, and dates.'
          }
        ]
      }
    ]
  },
  {
    id: 'elementary',
    name: 'Elementary',
    koreanName: '중초급',
    urduName: 'ابتدائی ثانوی سطح',
    description: 'Sentence structure (SOV), grammar particles (은/는, 이/가, 을/를, 에/에서), tenses, and daily conversations.',
    lessons: [
      {
        id: 'elem_01',
        title: 'Sentence Structure (SOV) & Particles',
        koreanTitle: '한국어 문장 구조 및 기본 조사',
        urduTitle: 'کورین جملے کی ساخت (SOV) اور حروفِ ربط',
        description: 'Korean follows Subject-Object-Verb order, identically matching Urdu sentence structure!',
        grammarNotes: 'In English, you say "I eat bread" (SVO). In Korean, you say "저는 빵을 먹어요" (SOV: Subject-Object-Verb). Urdu follows the exact same word order: "میں روٹی کھاتا ہوں"!',
        urduExplanation: 'اردو اور کورین کی گرامر میں حیرت انگیز مماثلت ہے: دونوں زبانوں میں فعل (Verb) ہمیشہ جملے کے آخر میں آتا ہے۔ اس لیے اردو بولنے والوں کے لیے کورین گرامر سیکھنا انتہائی آسان ہے۔',
        items: [
          {
            korean: '저는 밥을 먹어요.',
            romanization: 'Jeoneun babeul meogeoyo.',
            english: 'I eat rice / a meal.',
            urdu: 'میں کھانا کھاتا ہوں۔',
            exampleKorean: '민수는 한국어를 공부해요.',
            exampleEnglish: 'Minsu studies Korean.',
            exampleUrdu: 'من سو کورین زبان پڑھتا ہے۔'
          },
          {
            korean: '친구가 물을 마셔요.',
            romanization: 'Chinguga mureul masyeoyo.',
            english: 'The friend drinks water.',
            urdu: 'دوست پانی پیتا ہے۔',
            exampleKorean: '선생님이 책을 읽어요.',
            exampleEnglish: 'The teacher reads a book.',
            exampleUrdu: 'استاد کتاب پڑھتے ہیں۔'
          }
        ],
        quiz: [
          {
            question: 'In a standard Korean sentence, where is the main verb located?',
            urduQuestion: 'معیاری کورین جملے میں فعل کہاں واقع ہوتا ہے؟',
            options: ['At the start', 'Between subject and object', 'At the very end', 'Any position'],
            correctIndex: 2,
            explanation: 'Korean is strictly SOV; the verb always comes at the end.'
          }
        ]
      },
      {
        id: 'elem_02',
        title: 'Core Particles: 은/는, 이/가, 을/를',
        koreanTitle: '주제/주격/목적격 조사',
        urduTitle: 'موضوع، فاعل اور مفعول کے حروفِ ربط',
        description: 'Master the distinction between Topic (은/는), Subject (이/가), and Object (을/를) markers.',
        grammarNotes: '은/는 marks the main topic of conversation or comparison. 이/가 emphasizes the specific agent performing the verb. 을/를 marks direct object of action.',
        urduExplanation: 'حروفِ ربط (Particles) الفاظ کے بعد لگتے ہیں اور بتاتے ہیں کہ لفظ فاعل ہے یا مفعول۔ جیسے 은/는 موضوع کے لیے اور 을/를 مفعول (جس پر کام واقع ہوا ہو) کے لیے آتا ہے۔',
        items: [
          {
            korean: '은 / 는',
            romanization: 'Eun / Neun',
            english: 'Topic marker (As for...)',
            urdu: 'موضوعی علامت (جہاں تک تعلق ہے)',
            exampleKorean: '저는 파키스탄 사람이에요.',
            exampleEnglish: 'As for me, I am Pakistani.',
            exampleUrdu: 'جہاں تک میرا تعلق ہے، میں پاکستانی ہوں۔'
          },
          {
            korean: '이 / 가',
            romanization: 'I / Ga',
            english: 'Subject marker (Identifies who did it)',
            urdu: 'فاعلی علامت (کام کرنے والا کون ہے)',
            exampleKorean: '비가 와요.',
            exampleEnglish: 'Rain is falling.',
            exampleUrdu: 'بارش ہو رہی ہے۔'
          },
          {
            korean: '을 / 를',
            romanization: 'Eul / Reul',
            english: 'Object marker (Direct object of verb)',
            urdu: 'مفعولی علامت (جس پر کام ہوا ہو)',
            exampleKorean: '영화를 봐요.',
            exampleEnglish: 'I watch a movie.',
            exampleUrdu: 'میں فلم دیکھتا ہوں۔'
          }
        ],
        quiz: [
          {
            question: 'Which particle is attached to "저" (I/me) to set the topic?',
            urduQuestion: '"저" کے بعد موضوعی حرفِ ربط کون سا لگے گا؟',
            options: ['은', '는', '이', '을'],
            correctIndex: 1,
            explanation: 'Because 저 ends in a vowel, you attach 는 (저는).'
          }
        ]
      },
      {
        id: 'elem_03',
        title: 'Present, Past and Future Tenses',
        koreanTitle: '현재, 과거, 미래 시제',
        urduTitle: 'حال، ماضی اور مستقبل کے زمانے',
        description: 'Conjugate Korean verbs into polite everyday tenses: -아요/어요, -았어요/었어요, -(으)ㄹ 거예요.',
        grammarNotes: 'Base stem 가다 (to go): Present -> 가요; Past -> 갔어요; Future -> 갈 거예요.',
        urduExplanation: 'افعال کی گردان: حال کے لیے -아요/어요، ماضی کے لیے -았어요/었어요، اور مستقبل کے لیے -(으)ㄹ 거예요 لگایا جاتا ہے۔',
        items: [
          {
            korean: '지금 가요.',
            romanization: 'Jigeum gayo.',
            english: 'I am going now / I go now.',
            urdu: 'میں ابھی جا رہا ہوں۔',
            exampleKorean: '학교에 가요.',
            exampleEnglish: 'I go to school.',
            exampleUrdu: 'میں اسکول جاتا ہوں۔'
          },
          {
            korean: '어제 갔어요.',
            romanization: 'Eoje gasseoyo.',
            english: 'I went yesterday.',
            urdu: 'میں کل گیا تھا۔',
            exampleKorean: '어제 밥을 먹었어요.',
            exampleEnglish: 'I ate a meal yesterday.',
            exampleUrdu: 'میں نے کل کھانا کھایا تھا۔'
          },
          {
            korean: '내일 갈 거예요.',
            romanization: 'Naeil gal geoyeyo.',
            english: 'I will go tomorrow.',
            urdu: 'میں کل جاؤں گا۔',
            exampleKorean: '내일 공부할 거예요.',
            exampleEnglish: 'I will study tomorrow.',
            exampleUrdu: 'میں کل پڑھوں گا۔'
          }
        ],
        quiz: [
          {
            question: 'What is the past tense form of "보다" (to see/watch)?',
            urduQuestion: '"보다" (دیکھنا) کا ماضی کا صیغہ کیا ہے؟',
            options: ['봐요', '봤어요', '볼 거예요', '보세요'],
            correctIndex: 1,
            explanation: '봤어요 (bwasseoyo) is the polite past tense.'
          }
        ]
      }
    ]
  },
  {
    id: 'intermediate',
    name: 'Intermediate',
    koreanName: '중급',
    urduName: 'متوسط سطح',
    description: 'Complex connectors (-지만, -는데, -(으)니까, -(으)려고), natural dialogues, listening comprehension and idioms.',
    lessons: [
      {
        id: 'inter_01',
        title: 'Connectors: -지만 (But) & -는데 (Background)',
        koreanTitle: '연결 어미: -지만 과 -는데',
        urduTitle: 'ربطی کلمات: -지만 (لیکن) اور -는데 (پسِ منظر)',
        description: 'Connect contrasting ideas and provide smooth background context before making requests.',
        grammarNotes: '-지만 denotes contrast (A지만 B: although A, B). -는데/은데 provides contextual setup before an invitation, suggestion, or question.',
        urduExplanation: '-지만 کا مطلب "لیکن/مگر" ہے۔ جبکہ -는데 اگلے جملے یا درخواست کے لیے شائستہ پس منظر فراہم کرتا ہے۔',
        items: [
          {
            korean: '한국어는 어렵지만 재미있어요.',
            romanization: 'Hangugeoneun eoryeopjiman jaemiisseoyo.',
            english: 'Korean is difficult, but it is interesting.',
            urdu: 'کورین زبان مشکل ہے لیکن دلچسپ ہے۔',
            exampleKorean: '가격은 비싸지만 품질이 좋아요.',
            exampleEnglish: 'The price is expensive, but quality is high.',
            exampleUrdu: 'قیمت مہنگی ہے مگر معیار عمدہ ہے۔'
          },
          {
            korean: '지금 비가 오는데 우산이 있어요?',
            romanization: 'Jigeum biga oneunde usani isseoyo?',
            english: 'It is raining right now, do you have an umbrella?',
            urdu: 'ابھی بارش ہو رہی ہے، کیا آپ کے پاس چھتری ہے؟',
            exampleKorean: '배가 고픈데 같이 밥 먹을래요?',
            exampleEnglish: 'I feel hungry, would you like to eat together?',
            exampleUrdu: 'مجھے بھوک لگی ہے، کیا ساتھ کھانا کھائیں گے؟'
          }
        ],
        quiz: [
          {
            question: 'Choose the correct connector: "한국 음식은 맵___ 맛있어요" (Korean food is spicy BUT delicious).',
            urduQuestion: 'خالی جگہ پر کریں: کورین کھانا تیز مصالحے دار ہے لیکن لذیذ ہے۔',
            options: ['-지만', '-니까', '-려고', '-아서'],
            correctIndex: 0,
            explanation: '-지만 means "but / although" for contrasting descriptions.'
          }
        ]
      },
      {
        id: 'inter_02',
        title: 'Intentions: -(으)려고 하다',
        koreanTitle: '의도 표현: -(으)려고 하다',
        urduTitle: 'ارادہ اور منصوبے کا اظہار',
        description: 'Express your future intentions, plans, and aspirations.',
        grammarNotes: 'Attach -려고 하다 after vowel stems, -으려고 하다 after consonant stems.',
        urduExplanation: 'جب آپ اپنے مستقبل کے ارادے یا منصوبے کا اظہار کرنا چاہیں تو فعل کے ساتھ -(으)려고 하다 لگایا جاتا ہے۔',
        items: [
          {
            korean: '내년에 한국에 가려고 해요.',
            romanization: 'Naenyeone hanguge garyeogo haeyo.',
            english: 'I plan / intend to travel to South Korea next year.',
            urdu: 'میرا ارادہ اگلے سال جنوبی کوریا جانے کا ہے۔',
            exampleKorean: '토픽 4급을 따려고 해요.',
            exampleEnglish: 'I intend to obtain TOPIK Level 4.',
            exampleUrdu: 'میں ٹوپک لیول 4 پاس کرنے کا ارادہ رکھتا ہوں۔'
          }
        ],
        quiz: [
          {
            question: 'What meaning does the grammar pattern "-(으)려고 하다" convey?',
            urduQuestion: 'گرامر کا سانچہ "-(으)려고 하다" کس بات کا اظہار کرتا ہے؟',
            options: ['Intention or plan', 'Past regret', 'Command', 'Passive voice'],
            correctIndex: 0,
            explanation: 'It expresses intention or plans ("intend to...").'
          }
        ]
      }
    ]
  },
  {
    id: 'advanced',
    name: 'Advanced',
    koreanName: '고급',
    urduName: 'اعلیٰ / ایڈوانسڈ سطح',
    description: 'Speech levels, honorific suffixes, proverbs (속담), TOPIK II advanced grammar patterns, and business dialogues.',
    lessons: [
      {
        id: 'adv_01',
        title: 'Honorifics System & Speech Levels',
        koreanTitle: '높임말과 경어법 체계',
        urduTitle: 'کورین تعظیمی زبان اور احترامی مراتب',
        description: 'Learn subject honorific particle 께서, honorific verb infix -(으)시-, and humble vocabulary.',
        grammarNotes: 'Honorific nouns: 밥 -> 진지; 집 -> 댁; 나이 -> 연세. Honorific verbs: 주다 -> 드리다; 먹다 -> 드시다/잡수시다; 자다 -> 주무시다.',
        urduExplanation: 'اردو میں "آپ فرمائیے" کی طرح کورین میں بھی بزرگوں اور معزز افراد کے لیے مخصوص تعظیمی الفاظ (جیسے کھانا کے لیے 진지 اور گھر کے لیے 댁) استعمال کیے جاتے ہیں۔',
        items: [
          {
            korean: '할아버지께서 신문을 읽으십니다.',
            romanization: 'Harabeojikkeseo sinmuneul ilgeusimnida.',
            english: 'Grandfather is reading the newspaper (Formal honorific).',
            urdu: 'دادا جان اخبار پڑھ رہے ہیں۔',
            exampleKorean: '교수님께서 말씀하셨습니다.',
            exampleEnglish: 'The professor spoke.',
            exampleUrdu: 'پروفیسر صاحب نے ارشاد فرمایا۔'
          },
          {
            korean: '진지 잡수셨습니까?',
            romanization: 'Jinji jabsusyeosseumnikka?',
            english: 'Have you had your meal, sir/madam?',
            urdu: 'کیا آپ نے کھانا تناول فرما لیا؟',
            exampleKorean: '편안히 주무십시오.',
            exampleEnglish: 'Sleep peacefully (Good night to elder).',
            exampleUrdu: 'آرام سے سوئیے (شب بخیر)۔'
          }
        ],
        quiz: [
          {
            question: 'What is the respectful honorific equivalent of "밥" (meal/rice)?',
            urduQuestion: '"밥" (کھانا) کا تعظیمی متبادل لفظ کیا ہے؟',
            options: ['진지', '말씀', '연세', '성함'],
            correctIndex: 0,
            explanation: '진지 (jinji) is the honorific term for meal/rice.'
          }
        ]
      },
      {
        id: 'adv_02',
        title: 'TOPIK II Grammar & Proverbs (속담)',
        koreanTitle: '토픽 고급 문법: -기 마련이다 및 속담',
        urduTitle: 'ٹوپک اعلیٰ گرامر: -기 마련이다 اور کورین ضرب الامثال',
        description: 'Express natural inevitabilities and discover Korean cultural wisdom through classical proverbs.',
        grammarNotes: '-기 마련이다 indicates that something is bound or natural to happen as an inevitable outcome.',
        urduExplanation: '-기 마련이다 کا مطلب ہے "ایسا ہونا لازمی اور فطری ہے"۔ ضرب المثل "시작이 반이다" کا مطلب ہے "شروع کرنا ہی آدھی کامیابی ہے"۔',
        items: [
          {
            korean: '노력하면 성공하기 마련이다.',
            romanization: 'Noryeokhamyeon seonggonghagi maryeon-ida.',
            english: 'If you put in effort, you are bound to succeed.',
            urdu: 'اگر آپ محنت کریں گے تو کامیابی لازماً مل کر رہے گی۔',
            exampleKorean: '시간이 지나면 상처는 아물기 마련이에요.',
            exampleEnglish: 'With time, wounds are bound to heal.',
            exampleUrdu: 'وقت گزرنے پر زخم بھرنا قدرتی امر ہے۔'
          },
          {
            korean: '시작이 반이다.',
            romanization: 'Sijagi ban-ida.',
            english: 'Starting is half the battle (Well begun is half done).',
            urdu: 'آغاز ہی آدھا سفر ہوتا ہے (شروع کرنا ہی آدھی کامیابی ہے)۔',
            exampleKorean: '가는 말이 고와야 오는 말이 곱다.',
            exampleEnglish: 'Speak pleasant words to hear pleasant words in return.',
            exampleUrdu: 'جیسا کہو گے ویسا سنو گے (میٹھی زبان کا میٹھا جواب)۔'
          }
        ],
        quiz: [
          {
            question: 'What does the proverb "시작이 반이다" signify?',
            urduQuestion: '"시작이 반이다" کیا سکھاتی ہے؟',
            options: ['Starting is half the journey', 'Silence is golden', 'Time is money', 'Practice makes perfect'],
            correctIndex: 0,
            explanation: '시작이 반이다 means starting a task is already half the battle completed.'
          }
        ]
      }
    ]
  }
];

export const DICTIONARY_DATA: DictionaryEntry[] = [
  { korean: '안녕하세요', romanization: 'Annyeonghaseyo', english: 'Hello / Greetings', urdu: 'ہیلو / السلام علیکم', category: 'Greetings', level: 'Beginner' },
  { korean: '감사합니다', romanization: 'Gamsahamnida', english: 'Thank you', urdu: 'بہت شکریہ', category: 'Politeness', level: 'Beginner' },
  { korean: '죄송합니다', romanization: 'Joesonghamnida', english: 'I am sorry', urdu: 'معذرت خواہ ہوں', category: 'Politeness', level: 'Beginner' },
  { korean: '친구', romanization: 'Chingu', english: 'Friend', urdu: 'دوست', category: 'People', level: 'Beginner' },
  { korean: '선생님', romanization: 'Seonsaengnim', english: 'Teacher', urdu: 'استاد / معلم', category: 'People', level: 'Beginner' },
  { korean: '학생', romanization: 'Haksaeng', english: 'Student', urdu: 'طالب علم', category: 'People', level: 'Beginner' },
  { korean: '한국', romanization: 'Hanguk', english: 'South Korea', urdu: 'جنوبی کوریا', category: 'Places', level: 'Beginner' },
  { korean: '물', romanization: 'Mul', english: 'Water', urdu: 'پانی', category: 'Food & Drink', level: 'Beginner' },
  { korean: '밥', romanization: 'Bap', english: 'Cooked rice / Meal', urdu: 'کھانا / چاول', category: 'Food & Drink', level: 'Beginner' },
  { korean: '사과', romanization: 'Sagwa', english: 'Apple', urdu: 'سیب', category: 'Food & Drink', level: 'Beginner' },
  { korean: '공부하다', romanization: 'Gongbuhada', english: 'To study', urdu: 'مطالعہ کرنا / پڑھنا', category: 'Verbs', level: 'Elementary' },
  { korean: '먹다', romanization: 'Meokda', english: 'To eat', urdu: 'کھانا', category: 'Verbs', level: 'Elementary' },
  { korean: '가다', romanization: 'Gada', english: 'To go', urdu: 'جانا', category: 'Verbs', level: 'Elementary' },
  { korean: '오다', romanization: 'Oda', english: 'To come', urdu: 'آنا', category: 'Verbs', level: 'Elementary' },
  { korean: '도서관', romanization: 'Doseogwan', english: 'Library', urdu: 'لائبریری', category: 'Places', level: 'Elementary' },
  { korean: '지하철', romanization: 'Jihacheol', english: 'Subway / Metro', urdu: 'میٹرو ٹرین', category: 'Travel', level: 'Intermediate' },
  { korean: '행복하다', romanization: 'Haengbokhada', english: 'To be happy', urdu: 'خوش ہونا', category: 'Adjectives', level: 'Elementary' },
  { korean: '아름답다', romanization: 'Areumdapda', english: 'To be beautiful', urdu: 'خوبصورت ہونا', category: 'Adjectives', level: 'Intermediate' },
  { korean: '성공', romanization: 'Seonggong', english: 'Success', urdu: 'کامیابی', category: 'Abstract', level: 'Advanced' },
  { korean: '속담', romanization: 'Sokdam', english: 'Proverb / Idiom', urdu: 'کہاوت / ضرب المثل', category: 'Culture', level: 'Advanced' },
];

export const ADMOB_SPECS = {
  developer: 'Shahab Millizai',
  applicationId: 'com.shahabmillizai.learnkorean',
  realAppId: 'ca-app-pub-2790436784008422~8320873870',
  units: {
    banner: {
      name: 'Banner',
      realId: 'ca-app-pub-2790436784008422/7007792206',
      testId: 'ca-app-pub-3940256099942544/6300978111',
      placement: 'Bottom of Home, Lessons & Dictionary screens (non-overlapping)',
    },
    interstitial: {
      name: 'Interstitial',
      realId: 'ca-app-pub-2790436784008422/5419378368',
      testId: 'ca-app-pub-3940256099942544/1033173712',
      placement: 'Natural transitions after completing 3 lessons (never inside active quizzes)',
    },
    rewardedInterstitial: {
      name: 'Rewarded Interstitial',
      realId: 'ca-app-pub-2790436784008422/7366437749',
      testId: 'ca-app-pub-3940256099942544/5354046379',
      placement: 'Bonus TOPIK vocabulary packs, study hints, +150 XP reward with prior consent',
    },
    appOpen: {
      name: 'App Open',
      realId: 'ca-app-pub-2790436784008422/1048441408',
      testId: 'ca-app-pub-3940256099942544/9257395921',
      placement: 'Application cold launch and foreground return (4-hour cache limit)',
    },
    native: {
      name: 'Native Advanced',
      realId: 'ca-app-pub-2790436784008422/3681297429',
      testId: 'ca-app-pub-3940256099942544/2247696110',
      placement: 'Native learning feed cards with official AD attribution badge',
    },
  },
};
