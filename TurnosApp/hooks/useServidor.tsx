import TcpSocket from 'react-native-tcp-socket';
import { useRef } from 'react';

export function useServidor() {
  const IP_SERVIDOR = process.env.EXPO_PUBLIC_SERVER_IP || '192.168.0.19';
  const PUERTO = Number(process.env.EXPO_PUBLIC_SERVER_PORT) || 8080;

  // guardamos el cliente tcp para que no se cierre
  const clienteRef = useRef<any>(null);

  const conectar = (onActualizacion: (datos: any) => void) => {
    console.log("conectando por tcp...");

    clienteRef.current = TcpSocket.createConnection({
      port: PUERTO,
      host: IP_SERVIDOR,
    }, () => {
      console.log("conexion establecida con java");
    });

    clienteRef.current.on('data', (data: any) => {
      const respuesta = data.toString().trim();
      console.log("servidor responde:", respuesta);

      // pasamos el json de java a objeto para leerlo facil
      try {
        const datosJson = JSON.parse(respuesta);
        onActualizacion(datosJson);
      } catch (e) {
        console.log("no es json", respuesta);
      }
    });

    clienteRef.current.on('error', (error: any) => {
      console.log("fallo algo en el socket", error);
    });

    clienteRef.current.on('close', () => {
      console.log("conexion cerrada");
    });
  };

  const solicitarTurno = () => {
    if (clienteRef.current) {
      const datos = { accion: "PEDIR_TURNO" };
      const jsonEnviar = JSON.stringify(datos);
      
      console.log("enviando por tcp...", jsonEnviar);
      // importante el \n para que el bufferedreader de java lo lea
      clienteRef.current.write(jsonEnviar + '\n');
    }
  };

  return {
    conectar,
    solicitarTurno
  };
}