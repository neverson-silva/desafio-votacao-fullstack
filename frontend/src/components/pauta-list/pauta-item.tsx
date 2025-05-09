import { useState, useEffect } from 'react';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Clock, CheckCircle, XCircle } from 'lucide-react';
import { cn } from '@/lib/utils';
import dayjs from 'dayjs';
import type { Pauta } from '@/components/pauta-list';

interface PautaItemProps {
  pauta: Pauta;
  onOpenSession: (pauta: Pauta) => void;
  onOpenVoting: (pauta: Pauta) => void;
  onOpenResult: (pauta: Pauta) => void;
}

export const PautaItem = ({
  pauta,
  onOpenSession,
  onOpenVoting,
  onOpenResult,
}: PautaItemProps) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [timeRemaining, setTimeRemaining] = useState<string | null>(null);

  useEffect(() => {
    if (!pauta.sessao) {
      setIsOpen(false);
      return;
    }

    const checkSessionStatus = () => {
      const now = new Date();
      const startDate = new Date(pauta.sessao!.abertura);
      const endDate = new Date(pauta.sessao!.fechamento);

      const isSessionOpen = now >= startDate && now <= endDate;
      setIsOpen(isSessionOpen);

      if (isSessionOpen) {
        const remainingMs = endDate.getTime() - now.getTime();
        const remainingMinutes = Math.floor(remainingMs / 60000);
        const remainingSeconds = Math.floor((remainingMs % 60000) / 1000);

        if (remainingMinutes > 60) {
          const hours = Math.floor(remainingMinutes / 60);
          const mins = remainingMinutes % 60;
          setTimeRemaining(`${hours}h ${mins}m restantes`);
        } else {
          setTimeRemaining(
            `${remainingMinutes}m ${remainingSeconds}s restantes`,
          );
        }
      } else {
        setTimeRemaining(null);
      }
    };

    checkSessionStatus();

    const intervalId = setInterval(checkSessionStatus, 1000);

    return () => clearInterval(intervalId);
  }, [pauta.sessao]);

  return (
    <li
      className={cn(
        'p-3 sm:p-4 flex flex-col sm:flex-row sm:justify-between gap-3 sm:items-center hover:bg-muted/50 transition-colors',
        isOpen && 'bg-green-50 dark:bg-green-950/20',
      )}
    >
      <div className="flex flex-col gap-1">
        <div className="font-medium">{pauta?.titulo}</div>
        {pauta?.sessao && (
          <div className="text-xs text-muted-foreground flex flex-wrap items-center gap-1">
            <Clock size={12} />
            <span className="break-all">
              {dayjs(pauta.sessao?.abertura).format('DD/MM/YYYY HH:mm')} -{' '}
              {dayjs(pauta?.sessao?.fechamento).format('DD/MM/YYYY HH:mm')}
            </span>
            {timeRemaining && (
              <Badge
                variant="outline"
                className="ml-0 sm:ml-2 py-0 h-5 mt-1 sm:mt-0"
              >
                {timeRemaining}
              </Badge>
            )}
          </div>
        )}
      </div>
      <div className="flex items-center gap-2 self-start sm:self-center mt-2 sm:mt-0">
        {isOpen ? (
          <>
            <Badge className="bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-100 whitespace-nowrap">
              <CheckCircle size={12} className="mr-1" />
              Aberta
            </Badge>
            <Button
              variant="default"
              size="sm"
              className="h-6 text-xs whitespace-nowrap"
              onClick={() => onOpenVoting(pauta)}
            >
              Votar
            </Button>
          </>
        ) : (
          <>
            {pauta.sessao ? (
              <>
                <Badge
                  variant="destructive"
                  className="bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-100 whitespace-nowrap"
                >
                  <XCircle size={12} className="mr-1" />
                  Encerrada
                </Badge>
                <Button
                  size="sm"
                  className="h-5 whitespace-nowrap"
                  onClick={() => onOpenResult(pauta)}
                >
                  Resultado
                </Button>
              </>
            ) : (
              <Button
                size="sm"
                className="h-6 whitespace-nowrap"
                onClick={() => onOpenSession(pauta)}
              >
                Abrir sessão
              </Button>
            )}
          </>
        )}
      </div>
    </li>
  );
};
