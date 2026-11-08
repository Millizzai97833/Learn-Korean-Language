import JSZip from 'jszip';
import { ANDROID_PROJECT_FILES } from './projectFiles';

/**
 * Packages all native Android Studio files into a downloadable ZIP archive
 */
export async function downloadAndroidProjectZip(
  onProgress?: (percent: number, currentFile: string) => void
): Promise<void> {
  const zip = new JSZip();
  const rootFolder = zip.folder('LearnKoreanLanguage-Android');

  const total = ANDROID_PROJECT_FILES.length;

  for (let i = 0; i < total; i++) {
    const file = ANDROID_PROJECT_FILES[i];
    if (onProgress) {
      onProgress(Math.round(((i + 1) / total) * 100), file.path);
    }
    rootFolder?.file(file.path, file.content);
  }

  // Also include the assets/curriculum.json
  const curriculumResponse = await fetch('/curriculum.json').catch(() => null);
  if (curriculumResponse && curriculumResponse.ok) {
    const jsonText = await curriculumResponse.text();
    if (jsonText && !jsonText.startsWith('<!doctype') && !jsonText.startsWith('<!DOCTYPE') && !jsonText.startsWith('<html')) {
      rootFolder?.file('app/src/main/assets/curriculum.json', jsonText);
    }
  }

  const content = await zip.generateAsync({
    type: 'blob',
    compression: 'DEFLATE',
    compressionOptions: { level: 6 }
  });

  // Trigger browser download
  const url = URL.createObjectURL(content);
  const anchor = document.createElement('a');
  anchor.href = url;
  anchor.download = 'LearnKoreanLanguage-AndroidStudioProject.zip';
  document.body.appendChild(anchor);
  anchor.click();
  document.body.removeChild(anchor);
  URL.revokeObjectURL(url);
}
