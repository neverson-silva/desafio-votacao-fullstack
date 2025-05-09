import { PautaList } from '@/components/pauta-list';
import { ThemeProvider } from '@/components/theme-provider';
import { Toaster } from '@/components/ui/sonner';

function App() {
  return (
    <ThemeProvider defaultTheme="dark">
      <main className="flex h-screen justify-center items-center content-center  bg-background">
        <div className="w-2xl px-4 md:px-2">
          <PautaList />
        </div>
      </main>
      <Toaster />
    </ThemeProvider>
  );
}

export default App;
