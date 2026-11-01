import React, { useState, useEffect } from 'react';
import {
  Download,
  Smartphone,
  Code2,
  ShieldCheck,
  Terminal,
  Volume2,
  Flame,
  Zap,
  Check,
  Copy,
  ExternalLink,
  ChevronRight,
  Search,
  Bookmark,
  BookmarkCheck,
  BookOpen,
  Award,
  Sparkles,
  RefreshCw,
  Play,
  ArrowLeft,
  X,
  FileCode,
  FolderGit2,
  KeyRound,
  Eye,
  Layers,
  HelpCircle,
  Clock,
  Radio,
  Tv
} from 'lucide-react';

import {
  KOREAN_LEVELS,
  DICTIONARY_DATA,
  HANGUL_ALPHABET,
  ADMOB_SPECS,
  Level,
  Lesson,
  DictionaryEntry,
  HangulChar
} from './data/curriculumData';
import { ANDROID_PROJECT_FILES, ProjectFile } from './utils/projectFiles';
import { downloadAndroidProjectZip } from './utils/zipGenerator';
import { playKoreanSpeech } from './utils/speech';

export default function App() {
  // Navigation tabs for the web portal
  const [activePortalTab, setActivePortalTab] = useState<'simulator' | 'files' | 'admob' | 'guide'>('simulator');

  // Android Mobile Simulator State
  const [simBottomTab, setSimBottomTab] = useState<'home' | 'lessons' | 'practice' | 'dictionary' | 'profile'>('home');
  const [simTheme, setSimTheme] = useState<'light' | 'dark'>('light');
  const [useTestAds, setUseTestAds] = useState(true);

  // User Learning State in Simulator
  const [userXp, setUserXp] = useState(180);
  const [streakDays, setStreakDays] = useState(5);
  const [completedLessons, setCompletedLessons] = useState<string[]>(['beg_01']);
  const [bookmarkedWords, setBookmarkedWords] = useState<string[]>(['안녕하세요', '감사합니다']);
  const [bonusPackUnlocked, setBonusPackUnlocked] = useState(false);

  // Active Lesson Detail inside Simulator
  const [activeLessonId, setActiveLessonId] = useState<string | null>(null);
  const [lessonSubTab, setLessonSubTab] = useState<'phrases' | 'grammar' | 'quiz'>('phrases');
  const [quizIndex, setQuizIndex] = useState(0);
  const [quizSelectedOption, setQuizSelectedOption] = useState<number | null>(null);
  const [quizShowResult, setQuizShowResult] = useState(false);

  // Practice Screen State
  const [practiceTab, setPracticeTab] = useState<'hangul' | 'flashcards' | 'match' | 'topik'>('hangul');
  const [flashcardIndex, setFlashcardIndex] = useState(0);
  const [flashcardFlipped, setFlashcardFlipped] = useState(false);

  // Dictionary Search State
  const [searchQuery, setSearchQuery] = useState('');

  // AdMob Simulation Modals
  const [showRewardedModal, setShowRewardedModal] = useState(false);
  const [showRewardedAdPlayback, setShowRewardedAdPlayback] = useState(false);
  const [showInterstitialAdPlayback, setShowInterstitialAdPlayback] = useState(false);
  const [showAppOpenAdPlayback, setShowAppOpenAdPlayback] = useState(false);
  const [showPrivacyPolicyModal, setShowPrivacyPolicyModal] = useState(false);

  // Project Files Explorer State
  const [selectedFileIndex, setSelectedFileIndex] = useState(0);
  const [copiedFile, setCopiedFile] = useState(false);
  const [downloadingZip, setDownloadingZip] = useState(false);
  const [zipProgress, setZipProgress] = useState<{ percent: number; file: string } | null>(null);

  // Matching game state
  const [matchedPairs, setMatchedPairs] = useState<number[]>([]);
  const [selectedMatchKor, setSelectedMatchKor] = useState<number | null>(null);
  const [selectedMatchEng, setSelectedMatchEng] = useState<number | null>(null);

  const matchWords = [
    { id: 1, kor: '물', eng: 'Water (پانی)' },
    { id: 2, kor: '밥', eng: 'Meal / Rice (کھانا)' },
    { id: 3, kor: '친구', eng: 'Friend (دوست)' },
    { id: 4, kor: '선생님', eng: 'Teacher (استاد)' }
  ];

  const handleMatchSelectKor = (id: number) => {
    setSelectedMatchKor(id);
    if (selectedMatchEng !== null) {
      if (selectedMatchEng === id) {
        setMatchedPairs(prev => [...prev, id]);
        setUserXp(prev => prev + 25);
      }
      setSelectedMatchKor(null);
      setSelectedMatchEng(null);
    }
  };

  const handleMatchSelectEng = (id: number) => {
    setSelectedMatchEng(id);
    if (selectedMatchKor !== null) {
      if (selectedMatchKor === id) {
        setMatchedPairs(prev => [...prev, id]);
        setUserXp(prev => prev + 25);
      }
      setSelectedMatchKor(null);
      setSelectedMatchEng(null);
    }
  };

  const handleDownloadZip = async () => {
    setDownloadingZip(true);
    try {
      await downloadAndroidProjectZip((percent, file) => {
        setZipProgress({ percent, file });
      });
    } catch (err) {
      console.error(err);
      // Fallback to pre-built static zip
      window.location.href = '/LearnKoreanLanguage-AndroidStudioProject.zip';
    } finally {
      setTimeout(() => {
        setDownloadingZip(false);
        setZipProgress(null);
      }, 800);
    }
  };

  const handleCopyCode = () => {
    const file = ANDROID_PROJECT_FILES[selectedFileIndex];
    if (file) {
      navigator.clipboard.writeText(file.content);
      setCopiedFile(true);
      setTimeout(() => setCopiedFile(false), 2000);
    }
  };

  // Toggle word bookmark
  const toggleBookmark = (word: string) => {
    setBookmarkedWords(prev =>
      prev.includes(word) ? prev.filter(w => w !== word) : [...prev, word]
    );
  };

  // Current active lesson object
  const currentLesson: Lesson | undefined = activeLessonId
    ? KOREAN_LEVELS.flatMap(l => l.lessons).find(les => les.id === activeLessonId)
    : undefined;

  // Filter dictionary
  const filteredDictionary = DICTIONARY_DATA.filter(item => {
    if (!searchQuery.trim()) return true;
    const q = searchQuery.toLowerCase();
    return (
      item.korean.toLowerCase().includes(q) ||
      item.romanization.toLowerCase().includes(q) ||
      item.english.toLowerCase().includes(q) ||
      item.urdu.includes(searchQuery)
    );
  });

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans selection:bg-rose-600 selection:text-white">
      {/* Top Bar Contract: Zone 1 (Wordmark) — Zone 2 (4-5 text links) — Zone 3 (1 primary CTA) */}
      <header className="flex items-center justify-between gap-8 px-6 py-3.5 border-b border-slate-800 bg-slate-900/90 backdrop-blur sticky top-0 z-50">
        {/* Zone 1: Brand Wordmark */}
        <div className="flex items-center gap-3 shrink-0">
          <div className="w-8 h-8 rounded-full bg-gradient-to-b from-[#CD2E3A] to-[#0047A0] flex items-center justify-center text-white font-bold text-sm shadow-md ring-2 ring-white/20">
            한
          </div>
          <div>
            <div className="font-bold text-base tracking-tight text-white flex items-center gap-2">
              Learn Korean Language
            </div>
            <div className="text-[11px] text-slate-400 font-mono">
              com.shahabmillizai.learnkorean
            </div>
          </div>
        </div>

        {/* Zone 2: Navigation Links */}
        <nav className="hidden md:flex items-center gap-6 text-sm font-medium text-slate-300">
          <button
            onClick={() => setActivePortalTab('simulator')}
            className={`transition-colors whitespace-nowrap shrink-0 hover:text-white cursor-pointer ${
              activePortalTab === 'simulator' ? 'text-white font-semibold underline underline-offset-8 decoration-rose-500' : ''
            }`}
          >
            Live Android App
          </button>
          <button
            onClick={() => setActivePortalTab('files')}
            className={`transition-colors whitespace-nowrap shrink-0 hover:text-white cursor-pointer ${
              activePortalTab === 'files' ? 'text-white font-semibold underline underline-offset-8 decoration-rose-500' : ''
            }`}
          >
            Project Explorer ({ANDROID_PROJECT_FILES.length} Files)
          </button>
          <button
            onClick={() => setActivePortalTab('admob')}
            className={`transition-colors whitespace-nowrap shrink-0 hover:text-white cursor-pointer ${
              activePortalTab === 'admob' ? 'text-white font-semibold underline underline-offset-8 decoration-rose-500' : ''
            }`}
          >
            AdMob Monetization
          </button>
          <button
            onClick={() => setActivePortalTab('guide')}
            className={`transition-colors whitespace-nowrap shrink-0 hover:text-white cursor-pointer ${
              activePortalTab === 'guide' ? 'text-white font-semibold underline underline-offset-8 decoration-rose-500' : ''
            }`}
          >
            Build & Signing Guide
          </button>
        </nav>

        {/* Zone 3: 1 Primary CTA Button */}
        <div className="flex items-center gap-3 shrink-0">
          <button
            onClick={handleDownloadZip}
            disabled={downloadingZip}
            className="flex items-center gap-2 px-4 py-2 text-xs font-semibold text-white bg-gradient-to-r from-rose-600 to-indigo-600 rounded-lg hover:from-rose-500 hover:to-indigo-500 shadow-md transition-all active:scale-95 disabled:opacity-60 cursor-pointer"
          >
            <Download className="w-4 h-4" />
            <span>{downloadingZip ? 'Packaging ZIP...' : 'Download Project ZIP'}</span>
          </button>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="flex-1 flex flex-col">
        {/* ========================================================================= */}
        {/* TAB 1: LIVE ANDROID SIMULATOR */}
        {/* ========================================================================= */}
        {activePortalTab === 'simulator' && (
          <div className="flex-1 flex flex-col lg:flex-row items-center lg:items-start justify-center gap-8 p-6 lg:p-10 max-w-7xl mx-auto w-full">
            {/* Left Control & Inspector Sidebar */}
            <div className="w-full lg:w-80 flex flex-col gap-5 order-2 lg:order-1">
              {/* App Identity Card */}
              <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 shadow-sm">
                <div className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-2">
                  Project Specification
                </div>
                <h3 className="font-bold text-white text-base">Learn Korean Language</h3>
                <div className="text-xs text-slate-400 mt-1">
                  Developer: <span className="text-slate-200 font-medium">Shahab Millizai</span>
                </div>
                <div className="text-xs text-slate-400">
                  ID: <span className="font-mono text-indigo-400">com.shahabmillizai.learnkorean</span>
                </div>

                <div className="mt-4 pt-3 border-t border-slate-800 flex items-center justify-between text-xs text-slate-400">
                  <span>Target SDK: 35</span>
                  <span className="text-emerald-400">Kotlin 2.0 / Compose</span>
                </div>
              </div>

              {/* AdMob Quick Action Controls */}
              <div className="bg-slate-900 border border-slate-800 rounded-xl p-5 shadow-sm space-y-4">
                <div className="flex items-center justify-between">
                  <div className="text-xs font-bold uppercase tracking-wider text-slate-400">
                    AdMob Simulator Controls
                  </div>
                  <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                    Live SDK Ready
                  </span>
                </div>

                {/* Test Mode Switch */}
                <div className="flex items-center justify-between text-xs bg-slate-950 p-2.5 rounded-lg border border-slate-800">
                  <div>
                    <div className="font-medium text-slate-200">Use Test Ad IDs</div>
                    <div className="text-[11px] text-slate-500">
                      {useTestAds ? 'Google Sample IDs' : 'Shahab Millizai Real IDs'}
                    </div>
                  </div>
                  <button
                    onClick={() => setUseTestAds(!useTestAds)}
                    className={`w-10 h-5 flex items-center rounded-full p-0.5 transition-colors cursor-pointer ${
                      useTestAds ? 'bg-indigo-600 justify-end' : 'bg-slate-700 justify-start'
                    }`}
                  >
                    <div className="w-4 h-4 rounded-full bg-white shadow-md"></div>
                  </button>
                </div>

                {/* Ad Trigger Buttons for Interactive Testing */}
                <div className="space-y-2">
                  <button
                    onClick={() => setShowInterstitialAdPlayback(true)}
                    className="w-full flex items-center justify-between px-3 py-2 text-xs font-medium text-slate-300 bg-slate-800 hover:bg-slate-700 rounded-lg transition-colors cursor-pointer"
                  >
                    <span className="flex items-center gap-2">
                      <Tv className="w-3.5 h-3.5 text-amber-400" />
                      Test Interstitial Transition
                    </span>
                    <span className="text-[10px] font-mono text-slate-500">After 3 Lessons</span>
                  </button>

                  <button
                    onClick={() => setShowRewardedModal(true)}
                    className="w-full flex items-center justify-between px-3 py-2 text-xs font-medium text-slate-300 bg-slate-800 hover:bg-slate-700 rounded-lg transition-colors cursor-pointer"
                  >
                    <span className="flex items-center gap-2">
                      <Sparkles className="w-3.5 h-3.5 text-rose-400" />
                      Test Rewarded Interstitial
                    </span>
                    <span className="text-[10px] font-mono text-slate-500">+150 XP & Hints</span>
                  </button>

                  <button
                    onClick={() => setShowAppOpenAdPlayback(true)}
                    className="w-full flex items-center justify-between px-3 py-2 text-xs font-medium text-slate-300 bg-slate-800 hover:bg-slate-700 rounded-lg transition-colors cursor-pointer"
                  >
                    <span className="flex items-center gap-2">
                      <Radio className="w-3.5 h-3.5 text-blue-400" />
                      Test App Open Ad
                    </span>
                    <span className="text-[10px] font-mono text-slate-500">Launch / Return</span>
                  </button>
                </div>

                {/* Device Appearance */}
                <div className="pt-2 border-t border-slate-800 flex items-center justify-between text-xs text-slate-400">
                  <span>Device Theme:</span>
                  <div className="flex items-center gap-1 bg-slate-950 p-1 rounded-md border border-slate-800">
                    <button
                      onClick={() => setSimTheme('light')}
                      className={`px-2 py-0.5 rounded text-[11px] font-medium transition-colors cursor-pointer ${
                        simTheme === 'light' ? 'bg-white text-slate-900' : 'text-slate-400'
                      }`}
                    >
                      Light
                    </button>
                    <button
                      onClick={() => setSimTheme('dark')}
                      className={`px-2 py-0.5 rounded text-[11px] font-medium transition-colors cursor-pointer ${
                        simTheme === 'dark' ? 'bg-slate-800 text-white' : 'text-slate-400'
                      }`}
                    >
                      Dark
                    </button>
                  </div>
                </div>
              </div>

              {/* Native Android Project ZIP Banner */}
              <div className="bg-gradient-to-br from-indigo-950/80 to-slate-900 border border-indigo-800/40 rounded-xl p-5 shadow-sm">
                <div className="flex items-center gap-2 text-indigo-400 text-xs font-bold uppercase tracking-wider mb-2">
                  <FolderGit2 className="w-4 h-4" />
                  Ready For Android Studio
                </div>
                <p className="text-xs text-slate-300 leading-relaxed">
                  Includes root and app <code className="text-indigo-300 font-mono">build.gradle.kts</code>, AndroidManifest.xml with AdMob App ID, Jetpack Compose source, vector drawables, and GitHub Actions CI workflow.
                </p>
                <button
                  onClick={handleDownloadZip}
                  disabled={downloadingZip}
                  className="mt-3.5 w-full flex items-center justify-center gap-2 py-2.5 px-4 text-xs font-bold text-white bg-indigo-600 hover:bg-indigo-500 rounded-lg shadow transition-colors cursor-pointer"
                >
                  <Download className="w-3.5 h-3.5" />
                  <span>Download Complete ZIP</span>
                </button>
              </div>
            </div>

            {/* Android Device Hardware Frame */}
            <div className="order-1 lg:order-2 flex flex-col items-center">
              <div className="relative w-[380px] h-[780px] bg-slate-900 rounded-[48px] p-3 shadow-2xl ring-1 ring-slate-800 border-4 border-slate-700/80 flex flex-col overflow-hidden">
                {/* Physical Phone Speaker Notch & Camera */}
                <div className="absolute top-4 left-1/2 -translate-x-1/2 w-32 h-4 bg-slate-950 rounded-full z-40 flex items-center justify-center">
                  <div className="w-12 h-1 bg-slate-800 rounded-full"></div>
                  <div className="w-2.5 h-2.5 rounded-full bg-slate-900 ml-3 ring-1 ring-slate-800"></div>
                </div>

                {/* Screen Canvas Container */}
                <div
                  className={`flex-1 w-full rounded-[38px] flex flex-col overflow-hidden relative transition-colors ${
                    simTheme === 'dark' ? 'bg-[#121212] text-slate-100' : 'bg-[#F8FAFC] text-slate-900'
                  }`}
                >
                  {/* Android Status Bar */}
                  <div
                    className={`h-9 px-6 pt-2 flex items-center justify-between text-[11px] font-mono z-30 select-none ${
                      simTheme === 'dark' ? 'text-slate-400 bg-[#121212]' : 'text-slate-600 bg-[#F8FAFC]'
                    }`}
                  >
                    <span>09:41</span>
                    <div className="flex items-center gap-1.5">
                      <span>5G</span>
                      <div className="w-4 h-2 rounded-sm border border-current flex items-center p-0.5">
                        <div className="w-full h-full bg-current rounded-[1px]"></div>
                      </div>
                    </div>
                  </div>

                  {/* Android App Viewport */}
                  <div className="flex-1 overflow-y-auto flex flex-col">
                    {/* Screen 1: Lesson Detail View */}
                    {activeLessonId && currentLesson ? (
                      <div className="flex-1 flex flex-col">
                        {/* Detail Header */}
                        <div
                          className={`p-4 border-b flex items-center justify-between ${
                            simTheme === 'dark' ? 'border-slate-800 bg-[#1E1E24]' : 'border-slate-200 bg-white'
                          }`}
                        >
                          <button
                            onClick={() => {
                              setActiveLessonId(null);
                              setLessonSubTab('phrases');
                            }}
                            className="p-1 rounded-full hover:bg-slate-200 dark:hover:bg-slate-800 cursor-pointer"
                          >
                            <ArrowLeft className="w-5 h-5 text-slate-700 dark:text-slate-200" />
                          </button>
                          <div className="text-center">
                            <div className="font-bold text-sm truncate max-w-[200px]">{currentLesson.title}</div>
                            <div className="text-[11px] text-rose-500 font-medium">
                              {currentLesson.koreanTitle} · {currentLesson.urduTitle}
                            </div>
                          </div>
                          <div className="w-5"></div>
                        </div>

                        {/* Subtabs: Phrases, Grammar, Quiz */}
                        <div
                          className={`flex border-b text-xs font-medium ${
                            simTheme === 'dark' ? 'border-slate-800 bg-[#1E1E24]' : 'border-slate-200 bg-white'
                          }`}
                        >
                          <button
                            onClick={() => setLessonSubTab('phrases')}
                            className={`flex-1 py-2.5 text-center cursor-pointer transition-colors ${
                              lessonSubTab === 'phrases'
                                ? 'text-blue-600 dark:text-blue-400 border-b-2 border-blue-600 font-bold'
                                : 'text-slate-500'
                            }`}
                          >
                            Phrases ({currentLesson.items.length})
                          </button>
                          <button
                            onClick={() => setLessonSubTab('grammar')}
                            className={`flex-1 py-2.5 text-center cursor-pointer transition-colors ${
                              lessonSubTab === 'grammar'
                                ? 'text-blue-600 dark:text-blue-400 border-b-2 border-blue-600 font-bold'
                                : 'text-slate-500'
                            }`}
                          >
                            Grammar
                          </button>
                          <button
                            onClick={() => setLessonSubTab('quiz')}
                            className={`flex-1 py-2.5 text-center cursor-pointer transition-colors ${
                              lessonSubTab === 'quiz'
                                ? 'text-blue-600 dark:text-blue-400 border-b-2 border-blue-600 font-bold'
                                : 'text-slate-500'
                            }`}
                          >
                            Quiz ({currentLesson.quiz.length})
                          </button>
                        </div>

                        {/* Content for Phrases Subtab */}
                        {lessonSubTab === 'phrases' && (
                          <div className="p-4 space-y-3.5 flex-1 overflow-y-auto">
                            {currentLesson.items.map((item, idx) => (
                              <div
                                key={idx}
                                className={`p-3.5 rounded-xl border transition-all ${
                                  simTheme === 'dark'
                                    ? 'bg-[#1E1E24] border-slate-800'
                                    : 'bg-white border-slate-200 shadow-sm'
                                }`}
                              >
                                <div className="flex items-center justify-between">
                                  <span className="text-xl font-bold text-blue-600 dark:text-blue-400 font-korean">
                                    {item.korean}
                                  </span>
                                  <button
                                    onClick={() => playKoreanSpeech(item.korean)}
                                    className="p-1.5 rounded-full bg-blue-500/10 text-blue-600 dark:text-blue-400 hover:bg-blue-500/20 transition-colors cursor-pointer"
                                    title="Listen to Korean pronunciation"
                                  >
                                    <Volume2 className="w-4 h-4" />
                                  </button>
                                </div>
                                <div className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                                  {item.romanization}
                                </div>

                                <div className="mt-2.5 pt-2 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between text-xs">
                                  <div>
                                    <div className="text-[10px] text-slate-400">English</div>
                                    <div className="font-medium text-slate-800 dark:text-slate-200">{item.english}</div>
                                  </div>
                                  <div className="text-right">
                                    <div className="text-[10px] text-emerald-600 dark:text-emerald-400 font-urdu">اردو معنی</div>
                                    <div className="font-semibold text-emerald-700 dark:text-emerald-400 font-urdu">{item.urdu}</div>
                                  </div>
                                </div>

                                {item.exampleKorean && (
                                  <div className="mt-2.5 p-2 rounded-lg bg-slate-50 dark:bg-slate-900/60 text-[11px] space-y-0.5">
                                    <div className="font-semibold text-slate-700 dark:text-slate-300 font-korean">
                                      {item.exampleKorean}
                                    </div>
                                    <div className="text-slate-500">{item.exampleEnglish}</div>
                                    <div className="text-emerald-700 dark:text-emerald-400 font-urdu">{item.exampleUrdu}</div>
                                  </div>
                                )}
                              </div>
                            ))}

                            {/* Mark Lesson Complete Button */}
                            <button
                              onClick={() => {
                                if (!completedLessons.includes(currentLesson.id)) {
                                  setCompletedLessons(prev => [...prev, currentLesson.id]);
                                  setUserXp(prev => prev + 50);
                                }
                                // Trigger interstitial transition logic
                                setShowInterstitialAdPlayback(true);
                              }}
                              className="w-full py-3 bg-blue-600 hover:bg-blue-500 text-white rounded-xl font-bold text-xs shadow-md transition-all cursor-pointer flex items-center justify-center gap-2"
                            >
                              <Check className="w-4 h-4" />
                              <span>Mark Lesson Complete (+50 XP)</span>
                            </button>
                          </div>
                        )}

                        {/* Content for Grammar Subtab */}
                        {lessonSubTab === 'grammar' && (
                          <div className="p-4 space-y-4 flex-1 overflow-y-auto text-xs leading-relaxed">
                            <div
                              className={`p-4 rounded-xl border ${
                                simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                              }`}
                            >
                              <div className="font-bold text-blue-600 dark:text-blue-400 text-sm mb-2">
                                Grammar Explanation (English)
                              </div>
                              <p className="text-slate-700 dark:text-slate-300">{currentLesson.grammarNotes}</p>

                              <div className="my-3 border-t border-slate-100 dark:border-slate-800"></div>

                              <div className="font-bold text-emerald-600 dark:text-emerald-400 text-sm mb-1 text-right font-urdu">
                                گرامر کی مکمل وضاحت (اردو میں)
                              </div>
                              <p className="text-slate-800 dark:text-slate-200 text-right font-urdu leading-loose">
                                {currentLesson.urduExplanation}
                              </p>
                            </div>
                          </div>
                        )}

                        {/* Content for Quiz Subtab */}
                        {lessonSubTab === 'quiz' && currentLesson.quiz.length > 0 && (
                          <div className="p-4 flex-1 flex flex-col justify-between">
                            {(() => {
                              const q = currentLesson.quiz[quizIndex % currentLesson.quiz.length];
                              return (
                                <div className="space-y-4">
                                  <div className="flex items-center justify-between text-[11px] text-slate-500">
                                    <span>
                                      Question {quizIndex + 1} of {currentLesson.quiz.length}
                                    </span>
                                    <span className="text-blue-600 dark:text-blue-400 font-bold">+20 XP</span>
                                  </div>

                                  <div className="font-bold text-sm">{q.question}</div>
                                  <div className="text-xs text-emerald-600 dark:text-emerald-400 text-right font-urdu">
                                    {q.urduQuestion}
                                  </div>

                                  <div className="space-y-2 mt-2">
                                    {q.options.map((opt, oIdx) => {
                                      const isSelected = quizSelectedOption === oIdx;
                                      const isCorrect = quizShowResult && oIdx === q.correctIndex;
                                      const isWrong = quizShowResult && isSelected && oIdx !== q.correctIndex;

                                      return (
                                        <button
                                          key={oIdx}
                                          disabled={quizShowResult}
                                          onClick={() => setQuizSelectedOption(oIdx)}
                                          className={`w-full text-left p-3 rounded-xl border text-xs font-medium flex items-center justify-between transition-all cursor-pointer ${
                                            isCorrect
                                              ? 'bg-emerald-500/15 border-emerald-500 text-emerald-700 dark:text-emerald-300'
                                              : isWrong
                                              ? 'bg-rose-500/15 border-rose-500 text-rose-700 dark:text-rose-300'
                                              : isSelected
                                              ? 'bg-blue-500/15 border-blue-500 text-blue-700 dark:text-blue-300'
                                              : simTheme === 'dark'
                                              ? 'bg-[#1E1E24] border-slate-800'
                                              : 'bg-white border-slate-200 shadow-sm'
                                          }`}
                                        >
                                          <span>{opt}</span>
                                          {isCorrect && <Check className="w-4 h-4 text-emerald-600" />}
                                          {isWrong && <X className="w-4 h-4 text-rose-600" />}
                                        </button>
                                      );
                                    })}
                                  </div>

                                  {quizShowResult && (
                                    <div className="p-3 rounded-lg bg-blue-50 dark:bg-slate-900 border border-blue-200 dark:border-slate-800 text-[11px] text-slate-700 dark:text-slate-300">
                                      <span className="font-bold text-blue-600">Explanation: </span>
                                      {q.explanation}
                                    </div>
                                  )}
                                </div>
                              );
                            })()}

                            <div className="pt-4">
                              {!quizShowResult ? (
                                <button
                                  disabled={quizSelectedOption === null}
                                  onClick={() => {
                                    if (quizSelectedOption !== null) {
                                      setQuizShowResult(true);
                                      const currentQ = currentLesson.quiz[quizIndex % currentLesson.quiz.length];
                                      if (quizSelectedOption === currentQ.correctIndex) {
                                        setUserXp(prev => prev + 20);
                                      }
                                    }
                                  }}
                                  className="w-full py-2.5 bg-blue-600 disabled:opacity-50 text-white rounded-xl font-bold text-xs shadow transition-all cursor-pointer"
                                >
                                  Check Answer
                                </button>
                              ) : (
                                <button
                                  onClick={() => {
                                    if (quizIndex + 1 < currentLesson.quiz.length) {
                                      setQuizIndex(prev => prev + 1);
                                      setQuizSelectedOption(null);
                                      setQuizShowResult(false);
                                    } else {
                                      // Quiz completed!
                                      if (!completedLessons.includes(currentLesson.id)) {
                                        setCompletedLessons(prev => [...prev, currentLesson.id]);
                                        setUserXp(prev => prev + 30);
                                      }
                                      setActiveLessonId(null);
                                      setQuizIndex(0);
                                      setQuizSelectedOption(null);
                                      setQuizShowResult(false);
                                      setShowInterstitialAdPlayback(true);
                                    }
                                  }}
                                  className="w-full py-2.5 bg-blue-600 text-white rounded-xl font-bold text-xs shadow transition-all cursor-pointer"
                                >
                                  {quizIndex + 1 < currentLesson.quiz.length ? 'Next Question' : 'Finish Quiz (+30 XP)'}
                                </button>
                              )}
                            </div>
                          </div>
                        )}
                      </div>
                    ) : (
                      <>
                        {/* TAB A: HOME */}
                        {simBottomTab === 'home' && (
                          <div className="p-4 space-y-4 flex-1">
                            {/* Streak & XP Strip */}
                            <div className="flex items-center justify-between">
                              <div>
                                <h2 className="font-extrabold text-base tracking-tight">Learn Korean</h2>
                                <p className="text-[11px] text-slate-500">By Shahab Millizai</p>
                              </div>
                              <div className="flex items-center gap-2">
                                <div className="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-rose-500/10 text-rose-600 dark:text-rose-400 border border-rose-500/20 text-xs font-bold">
                                  <Flame className="w-3.5 h-3.5 text-rose-500" />
                                  <span>{streakDays}d</span>
                                </div>
                                <div className="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-blue-500/10 text-blue-600 dark:text-blue-400 border border-blue-500/20 text-xs font-bold">
                                  <Zap className="w-3.5 h-3.5 text-blue-500" />
                                  <span>{userXp} XP</span>
                                </div>
                              </div>
                            </div>

                            {/* Word of the Day Card */}
                            <div
                              className={`p-4 rounded-2xl border transition-all ${
                                simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                              }`}
                            >
                              <div className="flex items-center justify-between mb-1">
                                <span className="text-[10px] font-bold tracking-wider uppercase text-blue-600 dark:text-blue-400">
                                  Daily Word of the Day
                                </span>
                                <button
                                  onClick={() => playKoreanSpeech('안녕하세요')}
                                  className="p-1.5 rounded-full bg-blue-500/10 text-blue-600 hover:bg-blue-500/20 transition-colors cursor-pointer"
                                  title="Pronounce"
                                >
                                  <Volume2 className="w-3.5 h-3.5" />
                                </button>
                              </div>
                              <div className="text-2xl font-extrabold text-slate-900 dark:text-white font-korean">
                                안녕하세요
                              </div>
                              <div className="text-xs text-slate-500 dark:text-slate-400">Annyeonghaseyo</div>

                              <div className="mt-3 pt-2.5 border-t border-slate-100 dark:border-slate-800 flex items-center justify-between text-xs">
                                <div>
                                  <div className="text-[10px] text-slate-400">English</div>
                                  <div className="font-medium text-slate-700 dark:text-slate-300">Hello / Good day</div>
                                </div>
                                <div className="text-right">
                                  <div className="text-[10px] text-emerald-600 font-urdu">اردو ترجمہ</div>
                                  <div className="font-semibold text-emerald-700 dark:text-emerald-400 font-urdu">
                                    ہیلو / السلام علیکم
                                  </div>
                                </div>
                              </div>
                            </div>

                            {/* Hangul Trainer Quick Callout */}
                            <button
                              onClick={() => {
                                setSimBottomTab('practice');
                                setPracticeTab('hangul');
                              }}
                              className="w-full text-left p-3.5 rounded-2xl bg-gradient-to-r from-rose-500/10 via-blue-500/10 to-transparent border border-rose-500/30 flex items-center justify-between cursor-pointer group"
                            >
                              <div className="flex items-center gap-3">
                                <div className="w-10 h-10 rounded-xl bg-rose-600 text-white font-bold flex items-center justify-center text-lg shadow-sm">
                                  한
                                </div>
                                <div>
                                  <div className="font-bold text-xs text-slate-900 dark:text-white group-hover:text-rose-500 transition-colors">
                                    Hangul Alphabet Trainer
                                  </div>
                                  <div className="text-[11px] text-slate-500">24 consonants & vowels with sound</div>
                                </div>
                              </div>
                              <ChevronRight className="w-4 h-4 text-slate-400 group-hover:text-rose-500" />
                            </button>

                            {/* Rewarded Ad Bonus Pack Banner */}
                            <div
                              className={`p-3.5 rounded-2xl border flex items-center justify-between gap-3 ${
                                simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                              }`}
                            >
                              <div className="flex items-center gap-2.5">
                                <div className="w-8 h-8 rounded-lg bg-amber-500/15 text-amber-500 flex items-center justify-center">
                                  <Sparkles className="w-4 h-4" />
                                </div>
                                <div>
                                  <div className="font-bold text-xs">
                                    {bonusPackUnlocked ? 'Bonus Pack Active!' : 'Bonus TOPIK Vocab Pack'}
                                  </div>
                                  <div className="text-[10px] text-slate-500">
                                    {bonusPackUnlocked ? 'Earned +150 XP & hints' : 'Watch ad to unlock +150 XP'}
                                  </div>
                                </div>
                              </div>
                              {!bonusPackUnlocked && (
                                <button
                                  onClick={() => setShowRewardedModal(true)}
                                  className="px-2.5 py-1.5 bg-amber-600 hover:bg-amber-500 text-white rounded-lg text-[11px] font-bold shadow cursor-pointer transition-colors"
                                >
                                  Unlock
                                </button>
                              )}
                            </div>

                            {/* Course Levels Preview */}
                            <div className="space-y-2.5 pt-1">
                              <div className="flex items-center justify-between text-xs">
                                <span className="font-bold text-slate-800 dark:text-slate-200">Korean Curriculum</span>
                                <button
                                  onClick={() => setSimBottomTab('lessons')}
                                  className="text-[11px] text-blue-600 dark:text-blue-400 font-medium hover:underline cursor-pointer"
                                >
                                  View all →
                                </button>
                              </div>

                              {KOREAN_LEVELS.map(level => (
                                <div
                                  key={level.id}
                                  onClick={() => {
                                    setSimBottomTab('lessons');
                                  }}
                                  className={`p-3 rounded-xl border flex items-center justify-between cursor-pointer hover:border-blue-500/50 transition-all ${
                                    simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                                  }`}
                                >
                                  <div>
                                    <div className="flex items-center gap-2">
                                      <span className="font-bold text-xs">{level.name}</span>
                                      <span className="text-[11px] text-rose-500 font-bold">{level.koreanName}</span>
                                      <span className="text-[11px] text-emerald-600 font-urdu">{level.urduName}</span>
                                    </div>
                                    <div className="text-[10px] text-slate-500 mt-0.5 line-clamp-1">{level.description}</div>
                                  </div>
                                  <ChevronRight className="w-4 h-4 text-slate-400 shrink-0" />
                                </div>
                              ))}
                            </div>

                            {/* In-Feed Google Native Ad Demonstration */}
                            <div
                              className={`p-3.5 rounded-2xl border ${
                                simTheme === 'dark' ? 'bg-[#1E1E24]/70 border-slate-800' : 'bg-slate-100 border-slate-300'
                              }`}
                            >
                              <div className="flex items-center justify-between mb-1.5">
                                <span className="text-[9px] font-bold px-1.5 py-0.5 rounded bg-rose-600 text-white tracking-wider">
                                  SPONSORED AD
                                </span>
                                <span className="text-[10px] font-mono text-slate-400">Google Native Ad</span>
                              </div>
                              <div className="font-bold text-xs text-slate-900 dark:text-white">
                                Master Korean Faster with Daily Practice
                              </div>
                              <p className="text-[11px] text-slate-500 dark:text-slate-400 mt-1 line-clamp-2">
                                Verified Google Mobile Ads SDK integration adhering to AdMob native layout guidelines.
                              </p>
                              <button className="mt-2.5 w-full py-1.5 bg-[#0047A0] text-white rounded-lg text-xs font-semibold hover:bg-blue-800 transition-colors cursor-pointer">
                                Learn More
                              </button>
                            </div>
                          </div>
                        )}

                        {/* TAB B: LESSONS */}
                        {simBottomTab === 'lessons' && (
                          <div className="p-4 space-y-3.5 flex-1">
                            <div>
                              <h2 className="font-extrabold text-base tracking-tight">Structured Courses</h2>
                              <p className="text-[11px] text-slate-500">4 Levels: Beginner to Advanced TOPIK</p>
                            </div>

                            {KOREAN_LEVELS.map(level => (
                              <div key={level.id} className="space-y-2">
                                <div className="flex items-center justify-between text-xs font-bold pt-2 border-t border-slate-200 dark:border-slate-800">
                                  <span className="text-blue-600 dark:text-blue-400">
                                    {level.name} ({level.koreanName})
                                  </span>
                                  <span className="text-emerald-600 dark:text-emerald-400 font-urdu">{level.urduName}</span>
                                </div>

                                {level.lessons.map(lesson => {
                                  const isDone = completedLessons.includes(lesson.id);
                                  return (
                                    <div
                                      key={lesson.id}
                                      onClick={() => {
                                        setActiveLessonId(lesson.id);
                                        setLessonSubTab('phrases');
                                      }}
                                      className={`p-3 rounded-xl border flex items-center justify-between cursor-pointer transition-all hover:scale-[1.01] ${
                                        isDone
                                          ? 'border-blue-500/40 bg-blue-500/5'
                                          : simTheme === 'dark'
                                          ? 'bg-[#1E1E24] border-slate-800'
                                          : 'bg-white border-slate-200 shadow-sm'
                                      }`}
                                    >
                                      <div className="flex items-center gap-3">
                                        <div
                                          className={`w-7 h-7 rounded-full flex items-center justify-center ${
                                            isDone ? 'bg-blue-600 text-white' : 'bg-slate-200 dark:bg-slate-800 text-slate-400'
                                          }`}
                                        >
                                          {isDone ? <Check className="w-4 h-4" /> : <Play className="w-3 h-3 fill-current ml-0.5" />}
                                        </div>
                                        <div>
                                          <div className="font-bold text-xs">{lesson.title}</div>
                                          <div className="text-[10px] text-slate-500">
                                            {lesson.koreanTitle} · {lesson.items.length} phrases
                                          </div>
                                        </div>
                                      </div>
                                      <ChevronRight className="w-4 h-4 text-slate-400" />
                                    </div>
                                  );
                                })}
                              </div>
                            ))}
                          </div>
                        )}

                        {/* TAB C: PRACTICE */}
                        {simBottomTab === 'practice' && (
                          <div className="p-4 space-y-4 flex-1">
                            {/* Practice Mode Switcher */}
                            <div className="flex bg-slate-200 dark:bg-slate-800 p-1 rounded-xl text-[11px] font-medium">
                              <button
                                onClick={() => setPracticeTab('hangul')}
                                className={`flex-1 py-1.5 rounded-lg transition-colors cursor-pointer ${
                                  practiceTab === 'hangul' ? 'bg-white dark:bg-slate-900 text-blue-600 font-bold shadow' : 'text-slate-600 dark:text-slate-400'
                                }`}
                              >
                                Hangul
                              </button>
                              <button
                                onClick={() => setPracticeTab('flashcards')}
                                className={`flex-1 py-1.5 rounded-lg transition-colors cursor-pointer ${
                                  practiceTab === 'flashcards' ? 'bg-white dark:bg-slate-900 text-blue-600 font-bold shadow' : 'text-slate-600 dark:text-slate-400'
                                }`}
                              >
                                Cards
                              </button>
                              <button
                                onClick={() => setPracticeTab('match')}
                                className={`flex-1 py-1.5 rounded-lg transition-colors cursor-pointer ${
                                  practiceTab === 'match' ? 'bg-white dark:bg-slate-900 text-blue-600 font-bold shadow' : 'text-slate-600 dark:text-slate-400'
                                }`}
                              >
                                Match
                              </button>
                              <button
                                onClick={() => setPracticeTab('topik')}
                                className={`flex-1 py-1.5 rounded-lg transition-colors cursor-pointer ${
                                  practiceTab === 'topik' ? 'bg-white dark:bg-slate-900 text-blue-600 font-bold shadow' : 'text-slate-600 dark:text-slate-400'
                                }`}
                              >
                                TOPIK
                              </button>
                            </div>

                            {/* Mode 1: Hangul Alphabet Trainer */}
                            {practiceTab === 'hangul' && (
                              <div className="space-y-3">
                                <div className="text-[11px] text-slate-500">
                                  Tap any character to pronounce audio with Urdu & English phonetics:
                                </div>
                                <div className="grid grid-cols-3 gap-2 max-h-[460px] overflow-y-auto pr-1">
                                  {HANGUL_ALPHABET.map((item, idx) => (
                                    <button
                                      key={idx}
                                      onClick={() => playKoreanSpeech(item.char)}
                                      className={`p-2.5 rounded-xl border text-center transition-all hover:scale-105 active:scale-95 cursor-pointer ${
                                        simTheme === 'dark'
                                          ? 'bg-[#1E1E24] border-slate-800 hover:border-blue-500'
                                          : 'bg-white border-slate-200 shadow-sm hover:border-blue-500'
                                      }`}
                                    >
                                      <div className="text-xl font-extrabold text-blue-600 dark:text-blue-400 font-korean">
                                        {item.char}
                                      </div>
                                      <div className="text-[11px] font-bold text-slate-800 dark:text-slate-200">
                                        {item.name}
                                      </div>
                                      <div className="text-[10px] text-slate-400">{item.sound}</div>
                                      <div className="text-[11px] font-semibold text-emerald-600 dark:text-emerald-400 font-urdu mt-0.5">
                                        {item.urduSound}
                                      </div>
                                    </button>
                                  ))}
                                </div>
                              </div>
                            )}

                            {/* Mode 2: Flashcards */}
                            {practiceTab === 'flashcards' && DICTIONARY_DATA.length > 0 && (
                              <div className="flex flex-col items-center justify-center space-y-4 py-2">
                                <div className="text-xs text-slate-500">
                                  Card {flashcardIndex + 1} of {DICTIONARY_DATA.length} (Tap to Flip)
                                </div>

                                {(() => {
                                  const card = DICTIONARY_DATA[flashcardIndex % DICTIONARY_DATA.length];
                                  return (
                                    <div
                                      onClick={() => setFlashcardFlipped(!flashcardFlipped)}
                                      className={`w-full h-52 rounded-2xl border-2 p-6 flex flex-col items-center justify-center text-center cursor-pointer transition-all shadow-md ${
                                        flashcardFlipped
                                          ? 'bg-gradient-to-br from-emerald-500/10 to-transparent border-emerald-500'
                                          : 'bg-gradient-to-br from-blue-500/10 to-transparent border-blue-500'
                                      }`}
                                    >
                                      {!flashcardFlipped ? (
                                        <div className="space-y-2">
                                          <div className="text-3xl font-extrabold text-blue-600 dark:text-blue-400 font-korean">
                                            {card.korean}
                                          </div>
                                          <div className="text-sm text-slate-500">{card.romanization}</div>
                                          <button
                                            onClick={e => {
                                              e.stopPropagation();
                                              playKoreanSpeech(card.korean);
                                            }}
                                            className="p-2 rounded-full bg-blue-500/15 text-blue-600 mx-auto block hover:scale-110 cursor-pointer"
                                          >
                                            <Volume2 className="w-4 h-4" />
                                          </button>
                                        </div>
                                      ) : (
                                        <div className="space-y-2">
                                          <div className="text-lg font-bold text-slate-900 dark:text-white">
                                            {card.english}
                                          </div>
                                          <div className="text-xl font-bold text-emerald-600 font-urdu">
                                            {card.urdu}
                                          </div>
                                          <div className="text-[10px] text-slate-400 uppercase tracking-wider">
                                            {card.category} · {card.level}
                                          </div>
                                        </div>
                                      )}
                                    </div>
                                  );
                                })()}

                                <div className="flex items-center gap-3 w-full">
                                  <button
                                    onClick={() => {
                                      setFlashcardFlipped(false);
                                      setFlashcardIndex(prev => (prev > 0 ? prev - 1 : DICTIONARY_DATA.length - 1));
                                    }}
                                    className="flex-1 py-2 rounded-xl border border-slate-300 dark:border-slate-700 text-xs font-semibold cursor-pointer"
                                  >
                                    Previous
                                  </button>
                                  <button
                                    onClick={() => {
                                      setFlashcardFlipped(false);
                                      setFlashcardIndex(prev => (prev + 1) % DICTIONARY_DATA.length);
                                      setUserXp(prev => prev + 10);
                                    }}
                                    className="flex-1 py-2 rounded-xl bg-blue-600 text-white text-xs font-semibold shadow cursor-pointer"
                                  >
                                    Next (+10 XP)
                                  </button>
                                </div>
                              </div>
                            )}

                            {/* Mode 3: Word Matching Game */}
                            {practiceTab === 'match' && (
                              <div className="space-y-3">
                                <div className="text-xs text-slate-500">
                                  Match the Korean word with its English/Urdu translation (+25 XP each):
                                </div>

                                <div className="grid grid-cols-2 gap-3">
                                  {/* Korean column */}
                                  <div className="space-y-2">
                                    <div className="text-[11px] font-bold text-slate-400 text-center uppercase">Korean</div>
                                    {matchWords.map(w => {
                                      const isMatched = matchedPairs.includes(w.id);
                                      const isSelected = selectedMatchKor === w.id;
                                      return (
                                        <button
                                          key={w.id}
                                          disabled={isMatched}
                                          onClick={() => handleMatchSelectKor(w.id)}
                                          className={`w-full py-2.5 px-3 rounded-xl border text-xs font-bold text-center transition-all cursor-pointer ${
                                            isMatched
                                              ? 'bg-emerald-500/20 border-emerald-500 text-emerald-400 opacity-60 line-through'
                                              : isSelected
                                              ? 'bg-blue-600 text-white border-blue-600 shadow'
                                              : simTheme === 'dark'
                                              ? 'bg-[#1E1E24] border-slate-800'
                                              : 'bg-white border-slate-200'
                                          }`}
                                        >
                                          {w.kor}
                                        </button>
                                      );
                                    })}
                                  </div>

                                  {/* Translation column */}
                                  <div className="space-y-2">
                                    <div className="text-[11px] font-bold text-slate-400 text-center uppercase">English / Urdu</div>
                                    {matchWords
                                      .slice()
                                      .reverse()
                                      .map(w => {
                                        const isMatched = matchedPairs.includes(w.id);
                                        const isSelected = selectedMatchEng === w.id;
                                        return (
                                          <button
                                            key={w.id}
                                            disabled={isMatched}
                                            onClick={() => handleMatchSelectEng(w.id)}
                                            className={`w-full py-2.5 px-3 rounded-xl border text-xs font-medium text-center transition-all cursor-pointer ${
                                              isMatched
                                                ? 'bg-emerald-500/20 border-emerald-500 text-emerald-400 opacity-60 line-through'
                                                : isSelected
                                                ? 'bg-blue-600 text-white border-blue-600 shadow'
                                                : simTheme === 'dark'
                                                ? 'bg-[#1E1E24] border-slate-800'
                                                : 'bg-white border-slate-200'
                                            }`}
                                          >
                                            {w.eng}
                                          </button>
                                        );
                                      })}
                                  </div>
                                </div>

                                {matchedPairs.length === matchWords.length && (
                                  <div className="p-3 rounded-xl bg-emerald-500/15 border border-emerald-500 text-center text-xs text-emerald-400 font-bold">
                                    All Words Matched! Great job (+100 XP gained)
                                  </div>
                                )}
                              </div>
                            )}

                            {/* Mode 4: TOPIK Mock Exam */}
                            {practiceTab === 'topik' && (
                              <div className="space-y-3">
                                <div className="p-3 rounded-xl bg-rose-500/10 border border-rose-500/30">
                                  <div className="font-bold text-xs text-rose-500">TOPIK I / II Mock Question</div>
                                  <div className="text-[11px] text-slate-500 mt-0.5">
                                    빈칸에 들어갈 가장 알맞은 것을 고르십시오.
                                  </div>
                                </div>

                                <div
                                  className={`p-3.5 rounded-xl border text-xs space-y-2 ${
                                    simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                                  }`}
                                >
                                  <div className="font-semibold text-slate-800 dark:text-slate-200 leading-relaxed font-korean">
                                    가: 주말에 보통 뭐 해요?
                                    <br />
                                    나: 저는 집에서 영화를 (           ).
                                  </div>

                                  <div className="space-y-1.5 pt-2">
                                    {['1. 봅니다 (watch)', '2. 먹습니다 (eat)', '3. 갑니다 (go)', '4. 잡니다 (sleep)'].map(
                                      (opt, idx) => (
                                        <button
                                          key={idx}
                                          className={`w-full text-left p-2.5 rounded-lg border text-xs font-medium cursor-pointer ${
                                            idx === 0
                                              ? 'bg-blue-500/10 border-blue-500 text-blue-600 dark:text-blue-400 font-bold'
                                              : 'border-slate-200 dark:border-slate-800'
                                          }`}
                                        >
                                          {opt} {idx === 0 ? '✓ (Correct)' : ''}
                                        </button>
                                      )
                                    )}
                                  </div>
                                </div>
                              </div>
                            )}
                          </div>
                        )}

                        {/* TAB D: DICTIONARY */}
                        {simBottomTab === 'dictionary' && (
                          <div className="p-4 space-y-3 flex-1 flex flex-col">
                            <div>
                              <h2 className="font-extrabold text-base tracking-tight">Korean Dictionary</h2>
                              <p className="text-[11px] text-slate-500">Search Hangul, English, or اردو</p>
                            </div>

                            {/* Search Input */}
                            <div className="relative">
                              <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                              <input
                                type="text"
                                value={searchQuery}
                                onChange={e => setSearchQuery(e.target.value)}
                                placeholder="Search word..."
                                className={`w-full pl-9 pr-3 py-2 rounded-xl text-xs border outline-none transition-all ${
                                  simTheme === 'dark'
                                    ? 'bg-[#1E1E24] border-slate-800 focus:border-blue-500 text-white'
                                    : 'bg-white border-slate-200 focus:border-blue-500 text-slate-900'
                                }`}
                              />
                            </div>

                            <div className="text-[11px] text-slate-500">
                              Showing {filteredDictionary.length} entries
                            </div>

                            <div className="space-y-2 flex-1 overflow-y-auto pr-1">
                              {filteredDictionary.map((item, idx) => {
                                const isBookmarked = bookmarkedWords.includes(item.korean);
                                return (
                                  <div
                                    key={idx}
                                    className={`p-3 rounded-xl border flex items-center justify-between transition-all ${
                                      simTheme === 'dark'
                                        ? 'bg-[#1E1E24] border-slate-800'
                                        : 'bg-white border-slate-200 shadow-sm'
                                    }`}
                                  >
                                    <div className="flex items-center gap-2.5">
                                      <button
                                        onClick={() => playKoreanSpeech(item.korean)}
                                        className="p-1.5 rounded-full bg-blue-500/10 text-blue-600 hover:bg-blue-500/20 cursor-pointer"
                                      >
                                        <Volume2 className="w-3.5 h-3.5" />
                                      </button>
                                      <div>
                                        <div className="font-bold text-xs font-korean text-blue-600 dark:text-blue-400">
                                          {item.korean}
                                        </div>
                                        <div className="text-[10px] text-slate-400">{item.romanization}</div>
                                        <div className="text-xs text-slate-700 dark:text-slate-300 font-medium">
                                          {item.english}
                                        </div>
                                      </div>
                                    </div>

                                    <div className="flex items-center gap-2">
                                      <div className="text-right">
                                        <div className="text-xs font-semibold text-emerald-600 font-urdu">{item.urdu}</div>
                                        <div className="text-[9px] text-slate-400 uppercase">{item.category}</div>
                                      </div>
                                      <button
                                        onClick={() => toggleBookmark(item.korean)}
                                        className="p-1.5 text-slate-400 hover:text-rose-500 cursor-pointer"
                                      >
                                        {isBookmarked ? (
                                          <BookmarkCheck className="w-4 h-4 text-rose-500 fill-rose-500" />
                                        ) : (
                                          <Bookmark className="w-4 h-4" />
                                        )}
                                      </button>
                                    </div>
                                  </div>
                                );
                              })}
                            </div>
                          </div>
                        )}

                        {/* TAB E: PROFILE */}
                        {simBottomTab === 'profile' && (
                          <div className="p-4 space-y-4 flex-1 overflow-y-auto">
                            {/* Profile Header */}
                            <div
                              className={`p-4 rounded-2xl border text-center ${
                                simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                              }`}
                            >
                              <div className="w-14 h-14 rounded-full bg-blue-600 text-white font-bold text-xl flex items-center justify-center mx-auto shadow-md">
                                한
                              </div>
                              <h3 className="font-extrabold text-sm mt-2">Korean Language Scholar</h3>
                              <p className="text-[11px] text-slate-500">Developer: Shahab Millizai</p>

                              <div className="grid grid-cols-3 gap-2 mt-4 pt-3 border-t border-slate-100 dark:border-slate-800 text-center">
                                <div>
                                  <div className="text-base font-extrabold text-blue-600">{userXp}</div>
                                  <div className="text-[10px] text-slate-400">Total XP</div>
                                </div>
                                <div>
                                  <div className="text-base font-extrabold text-rose-600">{streakDays}d</div>
                                  <div className="text-[10px] text-slate-400">Streak</div>
                                </div>
                                <div>
                                  <div className="text-base font-extrabold text-emerald-600">{completedLessons.length}</div>
                                  <div className="text-[10px] text-slate-400">Lessons</div>
                                </div>
                              </div>
                            </div>

                            {/* AdMob Configuration Info */}
                            <div
                              className={`p-3.5 rounded-2xl border text-xs space-y-2 ${
                                simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                              }`}
                            >
                              <div className="font-bold text-slate-900 dark:text-white flex items-center justify-between">
                                <span>AdMob Monetization</span>
                                <span className="text-[10px] font-mono text-emerald-500">Google Verified</span>
                              </div>
                              <div className="text-[11px] text-slate-500 space-y-1 font-mono">
                                <div>App ID: ca-app-pub-2790436784008422~8320873870</div>
                                <div>Mode: {useTestAds ? 'Google Test Units' : 'Shahab Millizai Real Units'}</div>
                              </div>
                            </div>

                            {/* Privacy and UMP */}
                            <div
                              className={`p-3.5 rounded-2xl border text-xs space-y-2 ${
                                simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200 shadow-sm'
                              }`}
                            >
                              <div className="font-bold text-slate-900 dark:text-white">Privacy & Policy</div>
                              <button
                                onClick={() => setShowPrivacyPolicyModal(true)}
                                className="w-full text-left py-1.5 text-blue-600 dark:text-blue-400 hover:underline cursor-pointer flex items-center justify-between"
                              >
                                <span>View Privacy Policy & UMP Disclosures</span>
                                <ChevronRight className="w-3.5 h-3.5" />
                              </button>
                            </div>
                          </div>
                        )}
                      </>
                    )}
                  </div>

                  {/* Production Bottom Banner Ad Bar (Strictly Above Nav Buttons) */}
                  <div
                    className={`h-11 px-3 border-t flex items-center justify-between text-[11px] z-20 ${
                      simTheme === 'dark' ? 'bg-[#1A1A20] border-slate-800' : 'bg-slate-100 border-slate-200'
                    }`}
                  >
                    <div className="flex items-center gap-1.5">
                      <span className="text-[9px] font-bold px-1.5 py-0.5 rounded bg-amber-500 text-white">
                        AD
                      </span>
                      <span className="font-mono text-[10px] text-slate-500 truncate max-w-[190px]">
                        {useTestAds ? ADMOB_SPECS.units.banner.testId : ADMOB_SPECS.units.banner.realId}
                      </span>
                    </div>
                    <span className="text-[10px] text-slate-400 font-semibold">320×50 Adaptive</span>
                  </div>

                  {/* Android Bottom Navigation Bar (5 tabs: Home, Lessons, Practice, Dictionary, Profile) */}
                  <div
                    className={`h-14 border-t flex items-center justify-around z-30 select-none ${
                      simTheme === 'dark' ? 'bg-[#1E1E24] border-slate-800' : 'bg-white border-slate-200'
                    }`}
                  >
                    {[
                      { id: 'home', label: 'Home', icon: Smartphone },
                      { id: 'lessons', label: 'Lessons', icon: BookOpen },
                      { id: 'practice', label: 'Practice', icon: Award },
                      { id: 'dictionary', label: 'Dictionary', icon: Search },
                      { id: 'profile', label: 'Profile', icon: ShieldCheck }
                    ].map(tab => {
                      const IconComp = tab.icon;
                      const isActive = simBottomTab === tab.id && !activeLessonId;
                      return (
                        <button
                          key={tab.id}
                          onClick={() => {
                            setActiveLessonId(null);
                            setSimBottomTab(tab.id as any);
                          }}
                          className={`flex flex-col items-center justify-center gap-0.5 flex-1 py-1 cursor-pointer transition-colors ${
                            isActive ? 'text-blue-600 dark:text-blue-400 font-bold' : 'text-slate-400'
                          }`}
                        >
                          <IconComp className="w-4 h-4" />
                          <span className="text-[10px]">{tab.label}</span>
                        </button>
                      );
                    })}
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 2: PROJECT EXPLORER (Full Source Code & Files) */}
        {/* ========================================================================= */}
        {activePortalTab === 'files' && (
          <div className="flex-1 flex flex-col max-w-7xl mx-auto w-full p-6">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
              <div>
                <h2 className="text-xl font-bold text-white flex items-center gap-2">
                  <Code2 className="w-5 h-5 text-indigo-400" />
                  Android Studio Source Tree
                </h2>
                <p className="text-xs text-slate-400 mt-1">
                  Complete production code with Gradle build scripts, AdMob managers, and Jetpack Compose screens.
                </p>
              </div>

              <div className="flex items-center gap-3">
                <button
                  onClick={handleCopyCode}
                  className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold text-slate-300 bg-slate-800 hover:bg-slate-700 rounded-lg border border-slate-700 transition-colors cursor-pointer"
                >
                  {copiedFile ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{copiedFile ? 'Copied!' : 'Copy Code'}</span>
                </button>
                <button
                  onClick={handleDownloadZip}
                  disabled={downloadingZip}
                  className="flex items-center gap-1.5 px-4 py-1.5 text-xs font-semibold text-white bg-indigo-600 hover:bg-indigo-500 rounded-lg transition-colors cursor-pointer"
                >
                  <Download className="w-3.5 h-3.5" />
                  <span>Download ZIP</span>
                </button>
              </div>
            </div>

            <div className="flex-1 grid grid-cols-1 md:grid-cols-12 gap-6 min-h-[600px] pb-10">
              {/* File Selector Sidebar */}
              <div className="md:col-span-4 bg-slate-900 border border-slate-800 rounded-2xl p-4 flex flex-col overflow-y-auto max-h-[700px]">
                <div className="text-xs font-bold uppercase tracking-wider text-slate-400 mb-3 px-2">
                  Project Files ({ANDROID_PROJECT_FILES.length})
                </div>

                <div className="space-y-1">
                  {ANDROID_PROJECT_FILES.map((file, idx) => {
                    const isSelected = selectedFileIndex === idx;
                    return (
                      <button
                        key={idx}
                        onClick={() => setSelectedFileIndex(idx)}
                        className={`w-full text-left px-3 py-2.5 rounded-xl text-xs font-mono transition-colors flex items-center justify-between cursor-pointer ${
                          isSelected
                            ? 'bg-indigo-600 text-white font-bold'
                            : 'text-slate-300 hover:bg-slate-800 hover:text-white'
                        }`}
                      >
                        <div className="truncate mr-2">
                          <div className="truncate">{file.path}</div>
                          <div className={`text-[10px] font-sans truncate ${isSelected ? 'text-indigo-200' : 'text-slate-500'}`}>
                            {file.description}
                          </div>
                        </div>
                        <span
                          className={`text-[9px] uppercase px-1.5 py-0.5 rounded shrink-0 ${
                            isSelected ? 'bg-white/20 text-white' : 'bg-slate-800 text-slate-400'
                          }`}
                        >
                          {file.category}
                        </span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Code Viewer */}
              <div className="md:col-span-8 bg-slate-900 border border-slate-800 rounded-2xl flex flex-col overflow-hidden max-h-[700px]">
                {/* Code Viewer Header */}
                <div className="px-5 py-3 border-b border-slate-800 flex items-center justify-between bg-slate-950">
                  <div className="flex items-center gap-2 text-xs font-mono text-indigo-400">
                    <FileCode className="w-4 h-4" />
                    <span>/{ANDROID_PROJECT_FILES[selectedFileIndex].path}</span>
                  </div>
                  <div className="text-[11px] text-slate-400">
                    {ANDROID_PROJECT_FILES[selectedFileIndex].content.split('\n').length} lines
                  </div>
                </div>

                {/* Preformatted Code Content */}
                <div className="flex-1 p-5 overflow-auto font-mono text-xs leading-relaxed text-slate-300 bg-slate-950/80 select-text">
                  <pre>{ANDROID_PROJECT_FILES[selectedFileIndex].content}</pre>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 3: ADMOB INTEGRATION VERIFICATION */}
        {/* ========================================================================= */}
        {activePortalTab === 'admob' && (
          <div className="flex-1 max-w-6xl mx-auto w-full p-6 space-y-8">
            <div>
              <h2 className="text-xl font-bold text-white flex items-center gap-2">
                <ShieldCheck className="w-5 h-5 text-emerald-400" />
                Google AdMob Monetization Architecture
              </h2>
              <p className="text-xs text-slate-400 mt-1">
                Official Google Mobile Ads SDK integration for Developer Shahab Millizai (App ID: {ADMOB_SPECS.realAppId})
              </p>
            </div>

            {/* Units Comparison Matrix */}
            <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 shadow-sm overflow-x-auto">
              <div className="text-sm font-bold text-white mb-4">Ad Unit Verification & Routing</div>
              <table className="w-full text-left text-xs font-mono">
                <thead>
                  <tr className="border-b border-slate-800 text-slate-400 uppercase text-[10px]">
                    <th className="py-2.5">Ad Format</th>
                    <th className="py-2.5">Production Unit ID (Release Build)</th>
                    <th className="py-2.5">Google Test ID (Debug Build)</th>
                    <th className="py-2.5">Policy Placement Guard</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800 text-slate-300">
                  <tr>
                    <td className="py-3 font-sans font-bold text-white">Banner</td>
                    <td className="py-3 text-rose-400">{ADMOB_SPECS.units.banner.realId}</td>
                    <td className="py-3 text-slate-500">{ADMOB_SPECS.units.banner.testId}</td>
                    <td className="py-3 font-sans text-slate-400">{ADMOB_SPECS.units.banner.placement}</td>
                  </tr>
                  <tr>
                    <td className="py-3 font-sans font-bold text-white">Interstitial</td>
                    <td className="py-3 text-rose-400">{ADMOB_SPECS.units.interstitial.realId}</td>
                    <td className="py-3 text-slate-500">{ADMOB_SPECS.units.interstitial.testId}</td>
                    <td className="py-3 font-sans text-slate-400">{ADMOB_SPECS.units.interstitial.placement}</td>
                  </tr>
                  <tr>
                    <td className="py-3 font-sans font-bold text-white">Rewarded Interstitial</td>
                    <td className="py-3 text-rose-400">{ADMOB_SPECS.units.rewardedInterstitial.realId}</td>
                    <td className="py-3 text-slate-500">{ADMOB_SPECS.units.rewardedInterstitial.testId}</td>
                    <td className="py-3 font-sans text-slate-400">{ADMOB_SPECS.units.rewardedInterstitial.placement}</td>
                  </tr>
                  <tr>
                    <td className="py-3 font-sans font-bold text-white">App Open</td>
                    <td className="py-3 text-rose-400">{ADMOB_SPECS.units.appOpen.realId}</td>
                    <td className="py-3 text-slate-500">{ADMOB_SPECS.units.appOpen.testId}</td>
                    <td className="py-3 font-sans text-slate-400">{ADMOB_SPECS.units.appOpen.placement}</td>
                  </tr>
                  <tr>
                    <td className="py-3 font-sans font-bold text-white">Native Advanced</td>
                    <td className="py-3 text-rose-400">{ADMOB_SPECS.units.native.realId}</td>
                    <td className="py-3 text-slate-500">{ADMOB_SPECS.units.native.testId}</td>
                    <td className="py-3 font-sans text-slate-400">{ADMOB_SPECS.units.native.placement}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            {/* Google Policy Compliance Checklist */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
                <div className="font-bold text-sm text-white mb-2 flex items-center gap-2">
                  <Check className="w-4 h-4 text-emerald-400" />
                  Anti-Invalid Click Protection
                </div>
                <p className="text-xs text-slate-400 leading-relaxed">
                  Banners never cover navigation buttons or text. Ads are separated from clickable touch targets with safe margins.
                </p>
              </div>

              <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
                <div className="font-bold text-sm text-white mb-2 flex items-center gap-2">
                  <Check className="w-4 h-4 text-emerald-400" />
                  No Learning Disruption
                </div>
                <p className="text-xs text-slate-400 leading-relaxed">
                  Interstitials only display at natural transition points after 3 completed lessons. Active quizzes and tests are strictly protected.
                </p>
              </div>

              <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
                <div className="font-bold text-sm text-white mb-2 flex items-center gap-2">
                  <Check className="w-4 h-4 text-emerald-400" />
                  Prior Reward Disclosure
                </div>
                <p className="text-xs text-slate-400 leading-relaxed">
                  Users receive clear notice of the reward (+150 XP and bonus TOPIK vocabulary) before the rewarded interstitial plays, verified by SDK callbacks.
                </p>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 4: BUILD, TEST & RELEASE SIGNING GUIDE */}
        {/* ========================================================================= */}
        {activePortalTab === 'guide' && (
          <div className="flex-1 max-w-5xl mx-auto w-full p-6 space-y-8">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div>
                <h2 className="text-xl font-bold text-white flex items-center gap-2">
                  <Terminal className="w-5 h-5 text-indigo-400" />
                  Build APK on GitHub Actions & Local Android Studio
                </h2>
                <p className="text-xs text-slate-400 mt-1">
                  Automatic cloud APK compilation for Developer Shahab Millizai without needing Android Studio installed!
                </p>
              </div>

              <button
                onClick={handleDownloadZip}
                disabled={downloadingZip}
                className="flex items-center gap-2 px-4 py-2 bg-gradient-to-r from-rose-600 to-indigo-600 text-white rounded-xl text-xs font-bold shadow transition-all cursor-pointer hover:opacity-90 self-start md:self-auto"
              >
                <Download className="w-4 h-4" />
                <span>Download Ready Project ZIP</span>
              </button>
            </div>

            {/* Featured Hero Banner: 1-Click GitHub Cloud APK Builder */}
            <div className="bg-gradient-to-br from-indigo-950 via-slate-900 to-slate-900 border-2 border-indigo-500/40 rounded-2xl p-6 shadow-xl space-y-4">
              <div className="flex items-center justify-between flex-wrap gap-2">
                <div className="flex items-center gap-2.5">
                  <div className="w-9 h-9 rounded-xl bg-indigo-600 text-white flex items-center justify-center shadow">
                    <FolderGit2 className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-extrabold text-white text-base">GitHub Actions Cloud APK Builder</h3>
                    <p className="text-xs text-indigo-300">File: <code className="font-mono bg-indigo-950/60 px-1.5 py-0.5 rounded border border-indigo-700/50">.github/workflows/build-apk.yml</code></p>
                  </div>
                </div>

                <button
                  onClick={() => {
                    const workflowFile = ANDROID_PROJECT_FILES.find(f => f.path.includes('build-apk.yml'));
                    if (workflowFile) {
                      navigator.clipboard.writeText(workflowFile.content);
                      setCopiedFile(true);
                      setTimeout(() => setCopiedFile(false), 2000);
                    }
                  }}
                  className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold text-white bg-indigo-600 hover:bg-indigo-500 rounded-lg shadow transition-colors cursor-pointer"
                >
                  {copiedFile ? <Check className="w-3.5 h-3.5 text-emerald-300" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{copiedFile ? 'Workflow Copied!' : 'Copy Workflow Code'}</span>
                </button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-2">
                <div className="bg-slate-950/70 border border-slate-800 rounded-xl p-4 space-y-1.5">
                  <div className="text-indigo-400 text-xs font-bold uppercase tracking-wider flex items-center gap-1.5">
                    <span className="w-5 h-5 rounded-full bg-indigo-900 text-indigo-200 flex items-center justify-center text-[10px]">1</span>
                    Push to GitHub
                  </div>
                  <p className="text-[11px] text-slate-300 leading-relaxed">
                    Upload the downloaded ZIP or push the repo to GitHub. The <code className="text-indigo-300">.github/workflows/</code> folder is already set up.
                  </p>
                </div>

                <div className="bg-slate-950/70 border border-slate-800 rounded-xl p-4 space-y-1.5">
                  <div className="text-indigo-400 text-xs font-bold uppercase tracking-wider flex items-center gap-1.5">
                    <span className="w-5 h-5 rounded-full bg-indigo-900 text-indigo-200 flex items-center justify-center text-[10px]">2</span>
                    Click "Run Workflow"
                  </div>
                  <p className="text-[11px] text-slate-300 leading-relaxed">
                    Go to your repository on GitHub, open the <strong>Actions</strong> tab, click <strong>"Build Android APK"</strong>, and hit <strong>"Run workflow"</strong>.
                  </p>
                </div>

                <div className="bg-slate-950/70 border border-slate-800 rounded-xl p-4 space-y-1.5">
                  <div className="text-indigo-400 text-xs font-bold uppercase tracking-wider flex items-center gap-1.5">
                    <span className="w-5 h-5 rounded-full bg-indigo-900 text-indigo-200 flex items-center justify-center text-[10px]">3</span>
                    Download Ready APK
                  </div>
                  <p className="text-[11px] text-slate-300 leading-relaxed">
                    Once complete (~2 mins), scroll to <strong>Artifacts</strong> and click <strong>LearnKoreanLanguage-APKs</strong> to download the APK directly!
                  </p>
                </div>
              </div>
            </div>

            <div className="space-y-6">
              {/* Step 1 */}
              <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-3">
                <div className="flex items-center gap-2 font-bold text-white text-sm">
                  <div className="w-6 h-6 rounded-full bg-indigo-600 text-white flex items-center justify-center text-xs">1</div>
                  Pushing Code to GitHub (Command Line)
                </div>
                <div className="bg-slate-950 p-3.5 rounded-xl font-mono text-xs text-indigo-300 space-y-1 overflow-x-auto">
                  <div>git init</div>
                  <div>git add .</div>
                  <div>git commit -m "Initial commit: Learn Korean Language Android App"</div>
                  <div>git branch -M main</div>
                  <div>git remote add origin https://github.com/shahabmillizai/learn-korean-android.git</div>
                  <div>git push -u origin main</div>
                </div>
                <p className="text-xs text-slate-400">
                  Tip: You can also drag-and-drop the files onto github.com if you prefer not to use terminal commands!
                </p>
              </div>

              {/* Step 2 */}
              <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-3">
                <div className="flex items-center gap-2 font-bold text-white text-sm">
                  <div className="w-6 h-6 rounded-full bg-indigo-600 text-white flex items-center justify-center text-xs">2</div>
                  Building Locally in Android Studio (Optional)
                </div>
                <p className="text-xs text-slate-300 leading-relaxed">
                  1. Extract <code className="text-indigo-400 font-mono">LearnKoreanLanguage-AndroidStudioProject.zip</code>.<br />
                  2. Open Android Studio (Koala, Ladybug, or Iguana with JDK 17).<br />
                  3. Click <strong>File &gt; Open</strong> and choose the unzipped <code className="text-indigo-400 font-mono">LearnKoreanLanguage-Android</code> folder.<br />
                  4. Wait for Gradle sync to complete and click the green <strong>Run (▶)</strong> button!
                </p>
              </div>

              {/* Step 3 */}
              <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-3">
                <div className="flex items-center gap-2 font-bold text-white text-sm">
                  <div className="w-6 h-6 rounded-full bg-indigo-600 text-white flex items-center justify-center text-xs">3</div>
                  Generating Signed Release APK / AAB for Google Play Store
                </div>
                <div className="bg-slate-950 p-3.5 rounded-xl font-mono text-xs text-indigo-300 space-y-1 overflow-x-auto">
                  <div># 1. Generate release key</div>
                  <div>keytool -genkey -v -keystore learnkorean-release-key.jks \</div>
                  <div>&nbsp;&nbsp;-keyalg RSA -keysize 2048 -validity 10000 \</div>
                  <div>&nbsp;&nbsp;-alias learnkorean -storepass MySecretPass -keypass MySecretPass</div>
                  <br />
                  <div># 2. Build signed Android App Bundle for Google Play Store</div>
                  <div>./gradlew bundleRelease</div>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* ========================================================================= */}
      {/* INTERSTITIAL AD PLAYBACK SIMULATOR MODAL */}
      {/* ========================================================================= */}
      {showInterstitialAdPlayback && (
        <div className="fixed inset-0 bg-black/80 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-slate-900 border border-slate-700 rounded-3xl p-6 max-w-sm w-full shadow-2xl space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-rose-600 text-white font-mono">
                INTERSTITIAL AD
              </span>
              <button
                onClick={() => setShowInterstitialAdPlayback(false)}
                className="p-1 rounded-full bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white cursor-pointer"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="text-center py-6 space-y-2">
              <div className="w-16 h-16 rounded-2xl bg-indigo-600/20 border border-indigo-500/40 text-indigo-400 flex items-center justify-center mx-auto">
                <Tv className="w-8 h-8" />
              </div>
              <h3 className="font-bold text-base text-white">Full-Screen Interstitial Ad</h3>
              <p className="text-xs text-slate-400">
                Shown at natural breaks between lessons. Frequency limit: At most once every 3 completed lessons.
              </p>
              <div className="font-mono text-[10px] text-indigo-400 pt-2">
                Unit ID: {useTestAds ? ADMOB_SPECS.units.interstitial.testId : ADMOB_SPECS.units.interstitial.realId}
              </div>
            </div>

            <button
              onClick={() => setShowInterstitialAdPlayback(false)}
              className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-xs font-bold transition-colors cursor-pointer"
            >
              Continue to Lesson
            </button>
          </div>
        </div>
      )}

      {/* ========================================================================= */}
      {/* REWARDED INTERSTITIAL PROMPT MODAL */}
      {/* ========================================================================= */}
      {showRewardedModal && (
        <div className="fixed inset-0 bg-black/80 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-slate-900 border border-slate-700 rounded-3xl p-6 max-w-sm w-full shadow-2xl space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-amber-500 text-white font-mono">
                REWARD CONFIRMATION
              </span>
              <button
                onClick={() => setShowRewardedModal(false)}
                className="p-1 rounded-full bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white cursor-pointer"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="text-center py-4 space-y-2">
              <div className="w-14 h-14 rounded-2xl bg-amber-500/20 border border-amber-500/40 text-amber-400 flex items-center justify-center mx-auto">
                <Sparkles className="w-7 h-7" />
              </div>
              <h3 className="font-bold text-base text-white">Unlock Bonus TOPIK Pack?</h3>
              <p className="text-xs text-slate-300 leading-relaxed">
                Watch a short sponsored video to unlock exclusive TOPIK exam phrases, audio pronunciation hints, and <strong className="text-amber-400">+150 bonus XP</strong>!
              </p>
              <div className="font-mono text-[10px] text-slate-500 pt-1">
                Unit ID: {useTestAds ? ADMOB_SPECS.units.rewardedInterstitial.testId : ADMOB_SPECS.units.rewardedInterstitial.realId}
              </div>
            </div>

            <div className="flex items-center gap-3">
              <button
                onClick={() => setShowRewardedModal(false)}
                className="flex-1 py-2.5 bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-xl text-xs font-semibold cursor-pointer"
              >
                Maybe Later
              </button>
              <button
                onClick={() => {
                  setShowRewardedModal(false);
                  setShowRewardedAdPlayback(true);
                }}
                className="flex-1 py-2.5 bg-amber-600 hover:bg-amber-500 text-white rounded-xl text-xs font-bold cursor-pointer"
              >
                Watch Message
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ========================================================================= */}
      {/* REWARDED AD PLAYBACK VIDEO SIMULATOR */}
      {/* ========================================================================= */}
      {showRewardedAdPlayback && (
        <div className="fixed inset-0 bg-black/90 z-50 flex items-center justify-center p-4 backdrop-blur-md">
          <div className="bg-slate-900 border border-slate-700 rounded-3xl p-6 max-w-sm w-full shadow-2xl text-center space-y-4">
            <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-emerald-600 text-white font-mono">
              PLAYING SPONSORED MESSAGE
            </span>
            <div className="h-40 bg-slate-950 rounded-2xl flex flex-col items-center justify-center text-slate-400 space-y-2">
              <Tv className="w-8 h-8 text-amber-400 animate-pulse" />
              <div className="text-xs">Reward confirmed by Google Mobile Ads SDK</div>
            </div>
            <button
              onClick={() => {
                setShowRewardedAdPlayback(false);
                setBonusPackUnlocked(true);
                setUserXp(prev => prev + 150);
              }}
              className="w-full py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl text-xs font-bold shadow transition-colors cursor-pointer"
            >
              Claim +150 XP & Bonus Pack
            </button>
          </div>
        </div>
      )}

      {/* ========================================================================= */}
      {/* APP OPEN AD PLAYBACK SIMULATOR */}
      {/* ========================================================================= */}
      {showAppOpenAdPlayback && (
        <div className="fixed inset-0 bg-black/90 z-50 flex items-center justify-center p-4 backdrop-blur-md">
          <div className="bg-slate-900 border border-slate-700 rounded-3xl p-6 max-w-sm w-full shadow-2xl text-center space-y-4">
            <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-blue-600 text-white font-mono">
              APP OPEN AD SIMULATION
            </span>
            <div className="h-48 bg-slate-950 rounded-2xl flex flex-col items-center justify-center text-slate-400 space-y-2 p-4">
              <Radio className="w-8 h-8 text-blue-400 animate-pulse" />
              <div className="font-bold text-sm text-white">Learn Korean Language</div>
              <div className="text-xs text-slate-400">
                Shown when returning to app from background or on cold start (with 4-hour caching limit).
              </div>
            </div>
            <button
              onClick={() => setShowAppOpenAdPlayback(false)}
              className="w-full py-2.5 bg-blue-600 hover:bg-blue-500 text-white rounded-xl text-xs font-bold shadow transition-colors cursor-pointer"
            >
              Continue into App
            </button>
          </div>
        </div>
      )}

      {/* ========================================================================= */}
      {/* PRIVACY POLICY & UMP MODAL */}
      {/* ========================================================================= */}
      {showPrivacyPolicyModal && (
        <div className="fixed inset-0 bg-black/80 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-slate-900 border border-slate-700 rounded-3xl p-6 max-w-md w-full shadow-2xl space-y-4 max-h-[80vh] overflow-y-auto">
            <div className="flex items-center justify-between">
              <h3 className="font-bold text-base text-white">Privacy Policy & Disclosures</h3>
              <button
                onClick={() => setShowPrivacyPolicyModal(false)}
                className="p-1 rounded-full bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white cursor-pointer"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="text-xs text-slate-300 space-y-3 leading-relaxed">
              <p>
                <strong>Developer:</strong> Shahab Millizai<br />
                <strong>Application:</strong> Learn Korean Language (<code className="text-indigo-400">com.shahabmillizai.learnkorean</code>)
              </p>
              <p>
                • <strong>Offline Learning:</strong> All curriculum lessons, Hangul tables, grammar notes, and audio playback work offline without transmitting personal education data to third-party servers.
              </p>
              <p>
                • <strong>Google Mobile Ads & UMP Consent:</strong> We partner with Google AdMob to display advertisements. Google UMP is integrated to ensure compliance with European Economic Area (EEA) and UK privacy requirements.
              </p>
              <p>
                • <strong>Child & Age Policies:</strong> The app does not collect personal identifiers from children.
              </p>
            </div>

            <button
              onClick={() => setShowPrivacyPolicyModal(false)}
              className="w-full py-2.5 bg-indigo-600 text-white rounded-xl text-xs font-bold cursor-pointer"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
