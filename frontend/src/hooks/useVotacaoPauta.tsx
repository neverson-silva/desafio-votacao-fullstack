import { api } from '@/lib/api';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import type { AxiosError } from 'axios';
import { toast } from 'sonner';

export const useVotacaoPauta = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (requestData: {
      sessaoId: string;
      cpf: string;
      voto: 'SIM' | 'NAO';
    }) => {
      const response = await api.post(`/v1/votos`, requestData);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pautas'] });
      toast.success('Pauta votada com sucesso!');
    },
    onError: (error: AxiosError<any>) => {
      toast.error(
        error?.response?.data?.error ??
          'Erro ao votar na pauta. Tente novamente mais tarde.',
      );
    },
  });
};
