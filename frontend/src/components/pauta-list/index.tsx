import { api } from '@/lib/api';
import { useQuery } from '@tanstack/react-query';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { PlusCircle } from 'lucide-react';
import { Skeleton } from '@/components/ui/skeleton';
import { useState } from 'react';
import { NovaPautaForm } from '@/components/modals/nova-pauta-form';
import { AberturaSessaoForm } from '@/components/modals/abertura-sessao-form';
import { PautaItem } from '@/components/pauta-list/pauta-item';
import { VotacaoPautaForm } from '@/components/modals/votacao-pauta-form';
import { ResultadoVotacaoPauta } from '@/components/modals/resultado-votacao-pauta';

export interface Sessao {
  id: string;
  pauta: Pauta;
  abertura: string;
  fechamento: string;
}

export interface Pauta {
  id: string;
  titulo: string;
  descricao: string;
  sessao?: Sessao;
}

export const PautaList = () => {
  const { isLoading, data: pautas = [] } = useQuery<Pauta[]>({
    queryKey: ['pautas'],
    queryFn: async () => (await api.get('/v1/pautas')).data,
    refetchInterval: 50000,
  });

  const [isCreatingNew, setIsCreatingNew] = useState(false);
  const [isOpeningSession, setIsOpeningSession] = useState(false);
  const [isVoting, setIsVoting] = useState(false);
  const [pautaAtual, setPautaAtual] = useState<Pauta | null>(null);
  const [showingResult, setShowingResult] = useState(false);

  const handleOpenSession = (pauta: Pauta) => {
    setPautaAtual(pauta);
    setIsOpeningSession(true);
  };

  const handleShowResult = (pauta: Pauta) => {
    setPautaAtual(pauta);

    setShowingResult(true);
  };

  const handleOpenVoting = (pauta: Pauta) => {
    setPautaAtual(pauta);
    setIsVoting(true);
  };

  const SkeletonLoader = () => (
    <div className="space-y-3">
      {Array(3)
        .fill(0)
        .map((_, index) => (
          <div
            key={index}
            className="p-3 sm:p-4 flex flex-col sm:flex-row gap-3 sm:justify-between sm:items-center border-b"
          >
            <div className="space-y-2 w-full sm:w-2/3">
              <Skeleton className="h-5 w-full" />
              <Skeleton className="h-3 w-full sm:w-3/4" />
            </div>
            <div className="flex items-center gap-2 self-start sm:self-center mt-2 sm:mt-0">
              <Skeleton className="h-6 w-16" />
              <Skeleton className="h-8 w-20" />
            </div>
          </div>
        ))}
    </div>
  );

  return (
    <>
      <Card className="w-full max-w-5xl mx-auto shadow-md">
        <CardHeader className="border-b">
          <div className=" flex flex-col sm:flex-row sm:justify-between sm:items-center gap-3">
            <div className="w-full">
              <CardTitle className="text-xl font-bold">
                Pautas em Votação
              </CardTitle>
              <CardDescription className="text-sm text-muted-foreground mt-1">
                Lista de pautas disponíveis para votação
              </CardDescription>
            </div>
            <Button
              className="flex items-center gap-1 self-start"
              onClick={() => setIsCreatingNew(true)}
            >
              <PlusCircle size={16} />
              <span>Nova Pauta</span>
            </Button>
          </div>
        </CardHeader>
        <CardContent className="p-0">
          {isLoading ? (
            <SkeletonLoader />
          ) : pautas?.length === 0 ? (
            <div className="flex flex-col justify-center items-center h-40 text-center p-4">
              <p className="text-muted-foreground mb-2">
                Nenhuma pauta disponível no momento
              </p>
              <Button
                variant="outline"
                size="sm"
                className="flex items-center gap-1"
                onClick={() => setIsCreatingNew(true)}
              >
                <PlusCircle size={16} />
                <span>Criar Nova Pauta</span>
              </Button>
            </div>
          ) : (
            <ul className="divide-y">
              {pautas.map((pauta) => (
                <PautaItem
                  key={pauta.id}
                  pauta={pauta}
                  onOpenSession={handleOpenSession}
                  onOpenVoting={handleOpenVoting}
                  onOpenResult={handleShowResult}
                />
              ))}
            </ul>
          )}
        </CardContent>
        <CardFooter className="flex flex-col sm:flex-row sm:justify-between border-t p-4 text-xs text-muted-foreground gap-2">
          <div>Total de pautas: {pautas?.length || 0}</div>
          <div>Última atualização: {new Date().toLocaleString()}</div>
        </CardFooter>
      </Card>
      <NovaPautaForm
        open={isCreatingNew}
        onOpenChange={(valor) => setIsCreatingNew(valor)}
      />
      {pautaAtual && (
        <>
          <AberturaSessaoForm
            open={isOpeningSession}
            onOpenChange={(valor) => setIsOpeningSession(valor)}
            pauta={pautaAtual}
          />
          <VotacaoPautaForm
            open={isVoting}
            onOpenChange={(valor) => setIsVoting(valor)}
            pauta={pautaAtual}
          />
          {showingResult && (
            <ResultadoVotacaoPauta
              open={showingResult}
              onOpenChange={(valor) => setShowingResult(valor)}
              pauta={pautaAtual}
            />
          )}
        </>
      )}
    </>
  );
};
