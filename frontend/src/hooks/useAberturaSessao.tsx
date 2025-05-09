import { api } from '@/lib/api';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

export const useAberturaSessao = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      pautaId,
      duracao,
    }: {
      pautaId: string;
      duracao: number;
    }) => {
      const response = await api.post(`/v1/pautas/${pautaId}/sessoes`, {
        duracao,
      });
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pautas'] });
      toast.success('Sessão aberta para votação.');
    },
    onError: () => {
      toast.error(
        'Erro ao abrir sessão para votação desta pauta. Tente novamente mais tarde.',
      );
    },
  });
};
