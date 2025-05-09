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
import { Textarea } from '@/components/ui/textarea';
import { z } from 'zod';
import { Button } from '@/components/ui/button';
import { Loader2 } from 'lucide-react';
import { useCriarPauta } from '@/hooks/useCriarPauta';
import { useState } from 'react';

export type DialogFormProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

const formSchema = z.object({
  titulo: z
    .string()
    .min(3, {
      message: 'O título deve ter pelo menos 3 caracteres.',
    })
    .max(100, {
      message: 'O título não pode ter mais de 100 caracteres.',
    }),
  descricao: z
    .string()
    .min(10, {
      message: 'A descrição deve ter pelo menos 10 caracteres.',
    })
    .max(500, {
      message: 'A descrição não pode ter mais de 500 caracteres.',
    }),
});

export type NovaPautaFormValues = z.infer<typeof formSchema>;

export const NovaPautaForm: React.FC<DialogFormProps> = ({
  open,
  onOpenChange,
}) => {
  const { mutateAsync } = useCriarPauta();
  const [isLoading, setIsLoading] = useState(false);

  const form = useForm<NovaPautaFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      titulo: '',
      descricao: '',
    },
  });

  async function onSubmit(values: NovaPautaFormValues) {
    try {
      setIsLoading(true);
      await mutateAsync({
        titulo: values.titulo,
        descricao: values.descricao,
      });
      form.reset();
      handleOnOpenChange(false);
    } catch (error) {
      console.log(error);
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
            Nova Pauta para Votação
          </DialogTitle>
          <DialogDescription>
            Crie uma nova pauta para ser votada pelos cooperados. Preencha os
            campos abaixo com as informações necessárias.
          </DialogDescription>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="titulo"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Título da Pauta</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Ex: Aprovação do orçamento anual"
                      {...field}
                      disabled={isLoading}
                    />
                  </FormControl>
                  <FormDescription>
                    Um título claro e conciso para identificar a pauta.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="descricao"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Descrição</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Descreva detalhadamente o assunto da pauta e as informações relevantes para votação..."
                      className="min-h-[120px]"
                      {...field}
                      disabled={isLoading}
                    />
                  </FormControl>
                  <FormDescription>
                    Forneça detalhes suficientes para que os cooperados possam
                    tomar uma decisão informada.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter className="flex gap-3">
              <DialogClose asChild>
                <Button type="button" variant="outline" disabled={isLoading}>
                  Cancelar
                </Button>
              </DialogClose>
              <Button type="submit" disabled={isLoading}>
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Criando...
                  </>
                ) : (
                  'Criar Pauta'
                )}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};
