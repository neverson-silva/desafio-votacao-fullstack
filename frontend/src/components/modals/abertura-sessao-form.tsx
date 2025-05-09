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
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { z } from 'zod';
import { Button } from '@/components/ui/button';
import { Loader2, Clock } from 'lucide-react';
import { useState } from 'react';
import { Badge } from '@/components/ui/badge';
import type { Pauta } from '@/components/pauta-list';
import { useAberturaSessao } from '@/hooks/useAberturaSessao';
import { timeToSeconds } from '@/lib/utils';

export interface DialogFormProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  pauta: Pauta;
  onSuccess?: () => void;
}

const formSchema = z.object({
  duracao: z
    .string()
    .regex(/^([0-9]{2}):([0-5][0-9]):([0-5][0-9])$/, {
      message: 'Formato inválido. Use o formato HH:MM:SS (ex: 01:30:00)',
    })
    .refine(
      (value) => {
        const [hours, minutes, seconds] = value.split(':').map(Number);
        return hours > 0 || minutes > 0 || seconds > 0;
      },
      {
        message: 'A duração deve ser maior que zero',
      },
    ),
});

export type AberturaSessaoFormValues = z.infer<typeof formSchema>;

export const AberturaSessaoForm: React.FC<DialogFormProps> = ({
  open,
  onOpenChange,
  pauta,
  onSuccess,
}) => {
  const [isLoading, setIsLoading] = useState(false);
  const { mutateAsync } = useAberturaSessao();

  const form = useForm<AberturaSessaoFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      duracao: '00:01:00',
    },
  });

  async function onSubmit(values: AberturaSessaoFormValues) {
    try {
      setIsLoading(true);

      await mutateAsync({
        pautaId: pauta.id,
        duracao: timeToSeconds(values.duracao),
      });

      handleOnOpenChange(false);
      onSuccess?.();
    } catch (error) {
      console.error('Erro ao abrir sessão:', error);
    } finally {
      setIsLoading(false);
    }
  }
  const handleOnOpenChange = (open: boolean) => {
    if (!open) {
      form.reset();
    }
    onOpenChange(open);
  };

  return (
    <Dialog open={open} onOpenChange={handleOnOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-bold">
            Nova Sessão de Votação
          </DialogTitle>
          <DialogDescription>
            Defina a duração da sessão de votação para a pauta selecionada.
          </DialogDescription>
        </DialogHeader>

        <div className="my-4 p-4 bg-muted rounded-md">
          <h3 className="font-medium text-sm text-muted-foreground mb-1">
            Pauta selecionada:
          </h3>
          <div className="font-semibold text-lg">{pauta?.titulo}</div>
          <p className="text-sm mt-2 text-muted-foreground line-clamp-2">
            {pauta?.descricao}
          </p>
        </div>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="duracao"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Duração da Sessão</FormLabel>
                  <FormControl>
                    <div className="relative">
                      <Clock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                      <Input
                        placeholder="HH:MM:SS"
                        className="pl-10"
                        {...field}
                        disabled={isLoading}
                      />
                    </div>
                  </FormControl>
                  <FormDescription>
                    Defina por quanto tempo a sessão ficará aberta para votação
                    no formato horas:minutos:segundos.
                    <div className="mt-2 flex flex-wrap gap-2">
                      <Badge
                        variant="outline"
                        className="cursor-pointer hover:bg-primary/10"
                        onClick={() => form.setValue('duracao', '00:30:00')}
                      >
                        30 minutos
                      </Badge>
                      <Badge
                        variant="outline"
                        className="cursor-pointer hover:bg-primary/10"
                        onClick={() => form.setValue('duracao', '01:00:00')}
                      >
                        1 hora
                      </Badge>
                      <Badge
                        variant="outline"
                        className="cursor-pointer hover:bg-primary/10"
                        onClick={() => form.setValue('duracao', '02:00:00')}
                      >
                        2 horas
                      </Badge>
                      <Badge
                        variant="outline"
                        className="cursor-pointer hover:bg-primary/10"
                        onClick={() => form.setValue('duracao', '24:00:00')}
                      >
                        1 dia
                      </Badge>
                    </div>
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter className="flex gap-3 pt-2">
              <DialogClose asChild>
                <Button type="button" variant="outline" disabled={isLoading}>
                  Cancelar
                </Button>
              </DialogClose>
              <Button type="submit" disabled={isLoading}>
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Abrindo sessão...
                  </>
                ) : (
                  'Abrir Sessão'
                )}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};
