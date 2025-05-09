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
import { Loader2, ThumbsUp, ThumbsDown, User } from 'lucide-react';
import { useState } from 'react';
import type { Pauta } from '@/components/pauta-list';
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group';
import { Label } from '@/components/ui/label';
import dayjs from 'dayjs';
import { cpf as cpfValidator } from 'cpf-cnpj-validator';
import { useVotacaoPauta } from '@/hooks/useVotacaoPauta';

export interface VotacaoPautaFormProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  pauta: Pauta;
}

const formSchema = z.object({
  cpf: z
    .string()
    .min(11, { message: 'CPF deve ter 11 dígitos' })
    .max(14, { message: 'CPF inválido' })
    .refine((cpf) => cpfValidator.isValid(cpf), {
      message: 'CPF inválido',
    }),
  voto: z.enum(['SIM', 'NAO'], {
    required_error: 'Você precisa selecionar uma opção para votar',
  }),
});

export type VotacaoPautaFormValues = z.infer<typeof formSchema>;

export const VotacaoPautaForm: React.FC<VotacaoPautaFormProps> = ({
  open,
  onOpenChange,
  pauta,
}) => {
  const sessao = pauta.sessao;
  const [isLoading, setIsLoading] = useState(false);
  const { mutateAsync } = useVotacaoPauta();
  const form = useForm<VotacaoPautaFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      cpf: '',
    },
  });

  const formatCPF = (value: string) => {
    const cpfDigits = value.replace(/\D/g, '');

    if (cpfDigits.length <= 3) {
      return cpfDigits;
    } else if (cpfDigits.length <= 6) {
      return `${cpfDigits.slice(0, 3)}.${cpfDigits.slice(3)}`;
    } else if (cpfDigits.length <= 9) {
      return `${cpfDigits.slice(0, 3)}.${cpfDigits.slice(3, 6)}.${cpfDigits.slice(6)}`;
    } else {
      return `${cpfDigits.slice(0, 3)}.${cpfDigits.slice(3, 6)}.${cpfDigits.slice(6, 9)}-${cpfDigits.slice(9, 11)}`;
    }
  };

  const calcularTempoRestante = () => {
    const agora = new Date();
    const fechamento = new Date(sessao!.fechamento);
    const diferenca = fechamento.getTime() - agora.getTime();

    if (diferenca <= 0) return 'Votação encerrada';

    const minutos = Math.floor(diferenca / 60000);
    const segundos = Math.floor((diferenca % 60000) / 1000);

    if (minutos > 60) {
      const horas = Math.floor(minutos / 60);
      const mins = minutos % 60;
      return `${horas}h ${mins}m restantes`;
    }

    return `${minutos}m ${segundos}s restantes`;
  };

  async function onSubmit(values: VotacaoPautaFormValues) {
    try {
      setIsLoading(true);

      await mutateAsync({ ...values, sessaoId: pauta.sessao!.id });

      handleOnOpenChange(false);
    } catch (error) {
      console.error('Erro ao registrar voto:', error);
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
            Votação de Pauta
          </DialogTitle>
          <DialogDescription>
            Vote na pauta selecionada escolhendo uma das opções abaixo.
          </DialogDescription>
        </DialogHeader>

        <div className="my-4 p-4 bg-muted rounded-md">
          <h3 className="font-medium text-sm text-muted-foreground mb-1">
            Pauta em votação:
          </h3>
          <div className="font-semibold text-lg">{pauta?.titulo}</div>
          <p className="text-sm mt-2 text-muted-foreground">
            {pauta?.descricao}
          </p>

          {sessao && (
            <div className="mt-4 pt-3 border-t border-border flex justify-between items-center">
              <div className="text-xs text-muted-foreground">
                <span className="font-medium">Período de votação:</span>{' '}
                {dayjs(sessao!.abertura).format('DD/MM/YYYY HH:mm')} até{' '}
                {dayjs(sessao!.fechamento).format('DD/MM/YYYY HH:mm')}
              </div>
              <div className="text-xs font-medium text-primary">
                {calcularTempoRestante()}
              </div>
            </div>
          )}
        </div>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="cpf"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CPF do Cooperado</FormLabel>
                  <FormControl>
                    <div className="relative">
                      <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                      <Input
                        placeholder="000.000.000-00"
                        className="pl-10"
                        {...field}
                        value={formatCPF(field.value)}
                        onChange={(e) => {
                          field.onChange(e.target.value);
                        }}
                        disabled={isLoading}
                      />
                    </div>
                  </FormControl>
                  <FormDescription>
                    Informe seu CPF para identificação e registro do voto.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="voto"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <RadioGroup
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                      className="flex flex-col space-y-3"
                    >
                      <div className="flex items-center space-x-2 p-3 border rounded-md hover:bg-accent cursor-pointer">
                        <RadioGroupItem value="SIM" id="sim" />
                        <Label
                          htmlFor="sim"
                          className="flex items-center cursor-pointer"
                        >
                          <ThumbsUp className="mr-2 h-5 w-5 text-green-600" />
                          <span className="font-medium">Sim, aprovo</span>
                        </Label>
                      </div>

                      <div className="flex items-center space-x-2 p-3 border rounded-md hover:bg-accent cursor-pointer">
                        <RadioGroupItem value="NAO" id="nao" />
                        <Label
                          htmlFor="nao"
                          className="flex items-center cursor-pointer"
                        >
                          <ThumbsDown className="mr-2 h-5 w-5 text-red-600" />
                          <span className="font-medium">Não, rejeito</span>
                        </Label>
                      </div>
                    </RadioGroup>
                  </FormControl>
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
                    Registrando voto...
                  </>
                ) : (
                  'Confirmar Voto'
                )}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};
