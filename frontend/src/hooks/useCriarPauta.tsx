import { api } from '@/lib/api';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

export const useCriarPauta = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (pauta: { titulo: string; descricao: string }) => {
      const response = await api.post('/v1/pautas', pauta);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pautas'] });
      toast.success('Pauta criada com sucesso!');
    },
    onError: () => {
      toast.error('Erro ao criar pauta. Tente novamente mais tarde.');
    },
  });
};
