import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';

dayjs.extend(duration);

import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const timeToSeconds = (time: string) => {
  const [hours, minutes, seconds] = time.split(':').map(Number);
  const totalSeconds = dayjs.duration({ hours, minutes, seconds }).asSeconds();
  return totalSeconds;
};
