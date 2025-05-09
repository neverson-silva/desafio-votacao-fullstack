import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogClose,
} from '@/components/ui/dialog';
import type React from 'react';
import { Button } from '@/components/ui/button';
import { ThumbsUp, ThumbsDown, BarChart3, Users } from 'lucide-react';
import type { Pauta } from '@/components/pauta-list';
import dayjs from 'dayjs';
import { Progress } from '@/components/ui/progress';
import { Card } from '@/components/ui/card';
import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Skeleton } from '@/components/ui/skeleton';

export interface ResultadoVotacaoPautaProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  pauta: Pauta;
}

export const ResultadoVotacaoPauta: React.FC<ResultadoVotacaoPautaProps> = ({
  open,
  onOpenChange,
  pauta,
}) => {
  const sessao = pauta.sessao;

  console.log('PAUTA AQUI', pauta.id);

  const {
    data: resultados = { totalVotos: 0, votosNao: 0, votosSim: 0 },
    isLoading,
  } = useQuery<{
    totalVotos: number;
    votosSim: number;
    votosNao: number;
  }>({
    queryKey: ['resultadoVotacao', pauta.id],
    queryFn: async () => {
      const { data } = await api.get(`/v1/votos/pautas/${pauta?.id}/resultado`);
      return data;
    },
  });

  const porcentagemSim =
    resultados?.totalVotos > 0
      ? Math.round((resultados.votosSim / resultados.totalVotos) * 100)
      : 0;

  const porcentagemNao =
    resultados.totalVotos > 0
      ? Math.round((resultados.votosNao / resultados.totalVotos) * 100)
      : 0;

  const getResultadoVotacao = () => {
    if (resultados.totalVotos === 0) return 'Sem votos registrados';
    if (resultados.votosSim > resultados.votosNao) return 'APROVADA';
    if (resultados.votosNao > resultados.votosSim) return 'REJEITADA';
    return 'EMPATE';
  };

  const getResultadoClass = () => {
    if (resultados.totalVotos === 0) return 'text-gray-500';
    if (resultados.votosSim > resultados.votosNao) return 'text-green-600';
    if (resultados.votosNao > resultados.votosSim) return 'text-red-600';
    return 'text-amber-500';
  };

  const ResultadoSkeleton = () => (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Skeleton className="h-5 w-5 rounded-full" />
          <Skeleton className="h-5 w-32" />
        </div>
        <Skeleton className="h-7 w-16" />
      </div>

      <Card className="p-4">
        <div className="space-y-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-2">
              <Skeleton className="h-5 w-5 rounded-full" />
              <Skeleton className="h-5 w-24" />
            </div>
            <div className="flex items-center gap-2">
              <Skeleton className="h-7 w-10" />
              <Skeleton className="h-5 w-14" />
            </div>
          </div>
          <Skeleton className="h-2 w-full" />
        </div>
      </Card>

      <Card className="p-4">
        <div className="space-y-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-2">
              <Skeleton className="h-5 w-5 rounded-full" />
              <Skeleton className="h-5 w-24" />
            </div>
            <div className="flex items-center gap-2">
              <Skeleton className="h-7 w-10" />
              <Skeleton className="h-5 w-14" />
            </div>
          </div>
          <Skeleton className="h-2 w-full" />
        </div>
      </Card>

      <div className="flex items-center justify-center mt-4">
        <Skeleton className="h-5 w-5 rounded-full mr-2" />
        <Skeleton className="h-4 w-56" />
      </div>
    </div>
  );

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[550px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-bold">
            Resultado da Votação
          </DialogTitle>
          <DialogDescription>
            Confira o resultado da votação para a pauta selecionada.
          </DialogDescription>
        </DialogHeader>

        <div className="my-4 p-4 bg-muted rounded-md">
          <h3 className="font-medium text-sm text-muted-foreground mb-1">
            Pauta votada:
          </h3>
          <div className="font-semibold text-lg">{pauta?.titulo}</div>
          <p className="text-sm mt-2 text-muted-foreground">
            {pauta?.descricao}
          </p>

          {sessao && (
            <div className="mt-4 pt-3 border-t border-border flex flex-col sm:flex-row sm:justify-between sm:items-center gap-2">
              <div className="text-xs text-muted-foreground">
                <span className="font-medium">Período de votação:</span>{' '}
                {dayjs(sessao.abertura).format('DD/MM/YYYY HH:mm')} até{' '}
                {dayjs(sessao.fechamento).format('DD/MM/YYYY HH:mm')}
              </div>
              {isLoading ? (
                <Skeleton className="h-5 w-24" />
              ) : (
                <div className={`text-sm font-bold ${getResultadoClass()}`}>
                  {getResultadoVotacao()}
                </div>
              )}
            </div>
          )}
        </div>

        {isLoading ? (
          <ResultadoSkeleton />
        ) : (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Users className="h-5 w-5 text-muted-foreground" />
                <span className="font-medium">Total de votos:</span>
              </div>
              <span className="text-lg font-bold">{resultados.totalVotos}</span>
            </div>

            <Card className="p-4">
              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <div className="flex items-center gap-2">
                    <ThumbsUp className="h-5 w-5 text-green-600" />
                    <span className="font-medium">Votos "Sim"</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="font-bold text-lg">
                      {resultados.votosSim}
                    </span>
                    <span className="text-sm text-muted-foreground">
                      ({porcentagemSim}%)
                    </span>
                  </div>
                </div>
                <Progress value={porcentagemSim} className="h-2 bg-muted" />
              </div>
            </Card>

            <Card className="p-4">
              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <div className="flex items-center gap-2">
                    <ThumbsDown className="h-5 w-5 text-red-600" />
                    <span className="font-medium">Votos "Não"</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="font-bold text-lg">
                      {resultados.votosNao}
                    </span>
                    <span className="text-sm text-muted-foreground">
                      ({porcentagemNao}%)
                    </span>
                  </div>
                </div>
                <Progress value={porcentagemNao} className="h-2 bg-muted" />
              </div>
            </Card>

            <div className="flex items-center justify-center mt-4">
              <BarChart3 className="h-5 w-5 text-muted-foreground mr-2" />
              <span className="text-sm text-muted-foreground">
                Resultado apurado em {dayjs().format('DD/MM/YYYY [às] HH:mm')}
              </span>
            </div>
          </div>
        )}

        <DialogFooter className="flex justify-end pt-2">
          <DialogClose asChild>
            <Button type="button">Fechar</Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
